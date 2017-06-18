package com.ruijc.id;

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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * ID生成测试
 *
 * @author Storezhang
 * Created 2017-02-15 18:25
 * Email storezhang@gmail.com
 * QQ 160290688
 */
public class IdTests {

    @Test
    public void testSnowFlake() {
        testSnowFlake(10000000);
    }

    public void testSnowFlake(int num) {
        SnowFlakeGenerator snowFlake = new SnowFlakeGenerator(3, 10);

        long[] ids = new long[num];
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            ids[i] = snowFlake.next();
        }
        long end = System.currentTimeMillis();
        System.err.println("--->耗时：" + ((double) (end - start) / 1000) + "，生成ID个数：" + num);

        Set<Long> idSet = new HashSet<Long>((int) (num / 0.75));
        for (int i = 0; i < num; ++i) {
            idSet.add(ids[i]);
        }

        Assert.assertTrue(num == idSet.size());
    }

    @Test
    public void testFindSum() {
        int[] array = new int[] {1, 3, 4, 5, 6, 6, 7, 8, 9, 10, 11, 13};
        int start = 0;
        int end = array.length - 1;
        int result;
        for (int i = 0; i < array.length; ++i) {
            if (start >= end) {
                break;
            }

            result = array[start] + array[end];
            if (12 == result) {
                System.err.println("--->" + array[start] + ": " + array[end]);
                ++start;
            } else if (result < 12) {
                ++start;
            } else if (result > 12) {
                --end;
            }
        }
    }
}
