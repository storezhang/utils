package com.ruijc.mybatis;

import tk.mybatis.mapper.common.Mapper;

/**
 * 解决通用Mapper和Mybatis的Mapper注解冲突
 *
 * @author Storezhang
 */
public interface BaseMapper<T> extends Mapper<T> {
    // 解决通用Mapper和Mybatis的Mapper注解冲突
}