package com.ruijc.util;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author Storezhang
 */
public class CollectionUtils {

    /**
     * 获得最后一个元素
     *
     * @param <T>  泛型
     * @param data 数据
     * @return 最后一个元素
     */
    public static <T> T last(List<T> data) {
        return data.get(data.size() - 1);
    }

    /**
     * 组装一个列表的字符串形式
     *
     * @param <T>   类型
     * @param data  列表
     * @param split 分隔符
     * @return 字符串形式
     */
    public static <T> String strings(List<T> data, String split) {
        StringBuilder sb = new StringBuilder();

        if (null == data || data.isEmpty()) {
            sb.append("");
        } else {
            sb.append(split);
            for (T obj : data) {
                sb.append(obj.toString());
                sb.append(split);
            }
        }

        return sb.toString();
    }

    /**
     * 组装一个列表的字符串形式
     *
     * @param <T>  类型
     * @param data 列表
     * @return 字符串形式
     */
    public static <T> String strings(List<T> data) {
        return strings(data, "\n");
    }

    public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static boolean isBlank(byte[] data) {
        return (null == data || 0 == data.length);
    }

    public static <T> boolean isBlank(List<T> data) {
        return null == data || data.isEmpty();
    }

    public static <K, V> boolean isBlank(Map<K, V> data) {
        return null == data || data.isEmpty();
    }

    /**
     * 从数组中获得元素
     * @param data 数组
     * @param index 下标
     * @param <T> 类型
     * @return 元素
     */
    public static <T> T get(T[] data, int index) {
        int len = data.length;
        if (index > len - 1) {
            index = len - 1;
        }

        return data[index];
    }

    public static int get(int[] data, int index) {
        int len = data.length;
        if (index > len - 1) {
            index = len - 1;
        }

        return data[index];
    }

    public static long get(long[] data, int index) {
        int len = data.length;
        if (index > len - 1) {
            index = len - 1;
        }

        return data[index];
    }

    public static double get(double[] data, int index) {
        int len = data.length;
        if (index > len - 1) {
            index = len - 1;
        }

        return data[index];
    }

    public static float get(float[] data, int index) {
        int len = data.length;
        if (index > len - 1) {
            index = len - 1;
        }

        return data[index];
    }
}
