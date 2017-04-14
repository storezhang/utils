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

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 测试
 *
 * @author Storezhang
 * @create 2017-04-14 14:14
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public class SchedulerTests {

    @Test
    public void testScheduler() throws InterruptedException {
        final DelayScheduler scheduler = new DelayScheduler();
        scheduler.start();
        scheduler.add(new AbstractTask(1, 5) {
            public boolean doTask() {
                System.err.println("---->5S");

                return true;
            }
        });
        scheduler.add(new AbstractTask(2, 10) {
            public boolean doTask() {
                System.err.println("---->10S");

                return true;
            }
        });
        scheduler.add(new AbstractTask(3, 15) {
            public boolean doTask() {
                System.err.println("---->15S");

                return false;
            }
        });
        scheduler.setListener(new ITaskListener() {
            public void onSuccess(ITask task) {
                System.err.println("---->" + ((AbstractTask) task).getId() + "执行成功！");
            }

            public void onFailed(ITask task) {
                System.err.println("---->" + ((AbstractTask) task).getId() + "执行失败！");
                ((AbstractTask) task).after(5);
                scheduler.add((AbstractTask) task);
            }
        });
        TimeUnit.SECONDS.sleep(1000);
    }
}
