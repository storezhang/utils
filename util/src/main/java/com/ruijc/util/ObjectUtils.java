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

import java.lang.reflect.Field;

/**
 * 对象工具类
 *
 * @author Storezhang
 * Created 2017/9/21 下午6:14
 * Email storezhang@gmail.com
 * QQ 160290688
 */

public class ObjectUtils {

    public static <T> T fieldValue(Object obj, String fieldName) {
        T t = null;

        if (null == obj || StringUtils.isBlank(fieldName)) {
            return t;
        }


        Class<?> clazz = obj.getClass();
        Field field = null;
        for(; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
            } catch (Exception e) {
                continue;
            }
        }

        if (null != field) {
            field.setAccessible(true);
            try {
                t = (T) field.get(obj);
            } catch (Exception e) {
                t = null;
            }
        }

        return t;
    }
}
