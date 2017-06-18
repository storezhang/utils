package com.ruijc.task;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

import com.ruijc.structure.queue.IRingQueue;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 执行器
 *
 * @author Storezhang
 * Created 2017-04-14 13:45
 * Email storezhang@gmail.com
 * QQ 160290688
 */
public class Steper implements Runnable {

    private IRingQueue queue;
    private ThreadGroup threadGroup;
    private ThreadGroup stepGroup;
    private ExecutorService taskPool;
    private ExecutorService stepPool;
    private ITaskListener listener;

    public Steper(IRingQueue queue) {
        this(queue, null);
    }

    public Steper(IRingQueue queue, ITaskListener listener) {
        this.queue = queue;
        this.listener = listener;

        threadGroup = new ThreadGroup("taskGroup");
        stepGroup = new ThreadGroup("stepGroup");
        taskPool = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(threadGroup, runnable);
            }
        });

        stepPool = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(stepGroup, runnable);
            }
        });
    }

    public ITaskListener getListener() {
        return listener;
    }

    public void setListener(ITaskListener listener) {
        this.listener = listener;
    }

    public void run() {
        while (true) {
            int second = Calendar.getInstance().get(Calendar.MINUTE) * 60 + Calendar.getInstance().get(Calendar.SECOND);
            final Queue<IRingTask> tasks = queue.next(second);
            stepPool.execute(new Runnable() {
                public void run() {
                    final Iterator<IRingTask> it = tasks.iterator();
                    while (it.hasNext()) {
                        final IRingTask task = it.next();
                        if (task.getCycle() <= 0) {
                            taskPool.execute(new Runnable() {
                                public void run() {
                                    if (task.doTask()) {
                                        it.remove();
                                        if (null != listener) {
                                            listener.onSuccess(task);
                                        }
                                    } else {
                                        if (null != listener) {
                                            listener.onFailed(task);
                                        }
                                    }
                                }
                            });
                        } else {
                            task.countDown();
                        }
                    }
                }
            });

            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                continue;
            }
        }
    }
}
