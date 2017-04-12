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

import java.util.Queue;

/**
 * 抽象环形队列
 *
 * @author Storezhang
 * @create 2017-04-12 10:57
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public abstract class AbstractRingQueue<T> implements IRingQueue<T> {

    protected Slot[] slots;
    protected int length;

    public AbstractRingQueue() {
        this(3600);
    }

    public AbstractRingQueue(int length) {
        this.length = length;
        for (int i = 0; i < length; i++) {
            slots[i] = new Slot();
        }
    }

    public int getLength() {
        return length;
    }

    public abstract int index();

    public void add(T t) {
        add(index(), t);
    }

    public void remove(T t) {
        remove(index(), t);
    }

    public Queue<T> next(int index) {
        return slots[index % length].getElements();
    }

    public void add(int index, T t) {
        slots[index % length].add(t);
    }

    public void remove(int index, T t) {
        slots[index].reomve(t);
    }

    public void replace(T t) {
        remove(index(), t);
    }

    public void replace(int index, T t) {
        remove(index % length, t);
        add(index % length, t);
    }
}
