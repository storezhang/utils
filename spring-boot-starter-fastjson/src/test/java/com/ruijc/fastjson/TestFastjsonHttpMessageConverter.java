package com.ruijc.fastjson;

import com.ruijc.fastjson.converter.FastJsonHttpMessageConverter;

public class TestFastjsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    @Override
    protected String getResult(String old) {
        System.err.println("--->TestFastjsonHttpMessageConverter");
        return super.getResult(old);
    }
}
