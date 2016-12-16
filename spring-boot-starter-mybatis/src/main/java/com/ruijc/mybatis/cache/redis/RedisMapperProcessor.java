package com.ruijc.mybatis.cache.redis;

import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import java.util.Collections;
import java.util.Set;

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

/**
 * RedisMapper处理器
 *
 * @author Storezhang
 */
@AutoService(Processor.class)
public class RedisMapperProcessor extends BaseProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RedisMapper.class.getCanonicalName());
    }

    @Override
    protected RedisMapperProperties getMapperProperties(TypeElement clazzElement) {
        RedisMapperProperties properties = new RedisMapperProperties();
        RedisMapper redisMapperAnnotation = clazzElement.getAnnotation(RedisMapper.class);
        try {
            redisMapperAnnotation.implementation();
        } catch (MirroredTypeException mte) {
            properties.setImplementation(mte.getTypeMirror());
        }
        try {
            redisMapperAnnotation.eviction();
        } catch (MirroredTypeException mte) {
            properties.setEviction(mte.getTypeMirror());
        }
        properties.setBlocking(redisMapperAnnotation.blocking());
        properties.setFlushInterval(redisMapperAnnotation.flushInterval());
        properties.setReadWrite(redisMapperAnnotation.readWrite());
        properties.setSize(redisMapperAnnotation.size());

        return properties;
    }

    @Override
    protected Set<Element> getElement(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return (Set<Element>) roundEnv.getElementsAnnotatedWith(RedisMapper.class);
    }
}
