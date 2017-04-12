package com.ruijc.structure.queue;

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

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 环形队列插槽
 *
 * @author Storezhang
 * @create 2017-04-12 11:16
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public class Slot<T> {

    private Queue<T> elements;

    public Slot() {
        elements = new ConcurrentLinkedQueue<T>();
    }

    public Queue<T> getElements() {
        return elements;
    }

    public void add(T t) {
        elements.add(t);
    }

    public void reomve(T t) {
        Iterator<T> it = elements.iterator();
        while (it.hasNext()) {
            if (it.next().equals(t)) {
                it.remove();
            }
        }
    }
}
