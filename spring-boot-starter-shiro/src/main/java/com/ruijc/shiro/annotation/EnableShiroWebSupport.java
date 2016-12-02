package com.ruijc.shiro.annotation;

import com.ruijc.shiro.ShiroWebMvcConfigurerAdapter;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;

/**
 * 自动配置SpringMVC框架
 *
 * @author Storezhang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Import({EnableShiroWebSupport.ShiroWebMvcConfigurerAdapterImportSelector.class})
public @interface EnableShiroWebSupport {

    class ShiroWebMvcConfigurerAdapterImportSelector implements ImportSelector {
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{ShiroWebMvcConfigurerAdapter.class.getName()};
        }
    }
}
