package com.ruijc.mybatis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.annotation.PostConstruct;

@ConditionalOnClass(MapperScannerConfigurer.class)
@ConfigurationProperties("mybatis.mapper")
public class MapperProperties {

    private String mappers;
    private String notEmpty;
    private String identity;
    private String catalog;
    private String order;
    private String schema;
    private String seqFormat;
    private String style;
    private String enableMethodAnnotation;

    @PostConstruct
    public void init() {
        mappers = "tk.mybatis.mapper.common.Mapper";
        notEmpty = "true";
        identity = "MYSQL";
        catalog = "";
        schema = "";
        order = "AFTER";
        seqFormat = "{0}.nextval";
        style = "camelhump";
        enableMethodAnnotation = "false";
    }

    public String getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(String notEmpty) {
        this.notEmpty = notEmpty;
    }

    public String getMappers() {
        return mappers;
    }

    public void setMappers(String mappers) {
        this.mappers = mappers;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSeqFormat() {
        return seqFormat;
    }

    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getEnableMethodAnnotation() {
        return enableMethodAnnotation;
    }

    public void setEnableMethodAnnotation(String enableMethodAnnotation) {
        this.enableMethodAnnotation = enableMethodAnnotation;
    }
}
