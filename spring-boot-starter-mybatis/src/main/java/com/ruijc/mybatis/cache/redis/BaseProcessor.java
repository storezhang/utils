package com.ruijc.mybatis.cache.redis;

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

import com.squareup.javapoet.*;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * 处理器基类
 *
 * @author Storezhang
 */

public abstract class BaseProcessor extends AbstractProcessor {

    protected Filer filer;
    protected Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_6;
    }

    protected abstract RedisMapperProperties getMapperProperties(TypeElement clazzElement);

    protected abstract Set<Element> getElement(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : getElement(annotations, roundEnv)) {
            if (ElementKind.INTERFACE != element.getKind()) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("Only interfaces can be annotated with @%s", RedisMapper.class.getSimpleName()), element);
                return true;
            }

            TypeElement clazzElement = (TypeElement) element;

            RedisMapperProperties properties = getMapperProperties(clazzElement);
            TypeSpec.Builder clazzBuilder = makeClass(clazzElement, properties);
            makeMethod(clazzElement, clazzBuilder);

            write(clazzElement, clazzBuilder);
        }

        return true;
    }

    protected void write(TypeElement clazzElement, TypeSpec.Builder clazzBuilder) {
        JavaFile javaFile = JavaFile.builder(getPackageName(clazzElement), clazzBuilder.build()).build();

        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    String.format("Make interface has error with @%s or @%s", RedisMapper.class.getSimpleName(), LoggingRedisMapper.class.getSimpleName()),
                    clazzElement);
        }
    }

    protected String getPackageName(TypeElement clazzElement) {
        return clazzElement.getQualifiedName().toString().replace("." + clazzElement.getSimpleName(), "");
    }

    protected TypeSpec.Builder makeClass(TypeElement clazzElement, RedisMapperProperties properties) {
        TypeSpec.Builder clazzBuilder = TypeSpec.interfaceBuilder(clazzElement.getSimpleName().toString() + "$$Impl")
                .addModifiers(Modifier.PUBLIC);

        clazzBuilder.addAnnotation(Mapper.class);

        AnnotationSpec cacheAnnotation = AnnotationSpec.builder(CacheNamespace.class)
                .addMember("flushInterval", "$L", properties.getFlushInterval())
                .addMember("implementation", "$L.class", properties.getImplementation())
                .addMember("eviction", "$L.class", properties.getEviction())
                .addMember("size", "$L", properties.getSize())
                .addMember("readWrite", "$L", properties.isReadWrite())
                .addMember("blocking", "$L", properties.isBlocking())
                .build();
        clazzBuilder.addAnnotation(cacheAnnotation);

        for (TypeMirror interfaceClass : clazzElement.getInterfaces()) {
            clazzBuilder.addSuperinterface(TypeName.get(interfaceClass));
        }
        clazzBuilder.addSuperinterface(TypeName.get(clazzElement.asType()));

        return clazzBuilder;
    }

    protected void makeMethod(TypeElement clazzElement, TypeSpec.Builder clazzBuilder) {
        for (Element encloseElement : clazzElement.getEnclosedElements()) {
            if (ElementKind.METHOD == encloseElement.getKind()) {
                ExecutableElement methodElement = (ExecutableElement) encloseElement;

                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(TypeName.get(methodElement.getReturnType()));

                for (VariableElement paramElement : methodElement.getParameters()) {
                    ParameterSpec.Builder paramBuilder = ParameterSpec.builder(TypeName.get(paramElement.asType()), paramElement.getSimpleName().toString());
                    for (AnnotationMirror annotationMirror : paramElement.getAnnotationMirrors()) {
                        paramBuilder.addAnnotation(AnnotationSpec.get(annotationMirror));
                    }
                    methodBuilder.addParameter(paramBuilder.build());
                }

                for (TypeMirror expMirror : methodElement.getThrownTypes()) {
                    methodBuilder.addException(TypeName.get(expMirror));
                }

                MethodSpec method = methodBuilder.build();
                clazzBuilder.addMethod(method);
            }
        }
    }
}
