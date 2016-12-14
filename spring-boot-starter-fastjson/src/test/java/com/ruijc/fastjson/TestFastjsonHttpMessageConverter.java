package com.ruijc.fastjson;

import com.ruijc.fastjson.converter.MyFastJsonHttpMessageConverter;

public class TestFastjsonHttpMessageConverter extends MyFastJsonHttpMessageConverter {

    @Override
    protected String getResult(String old) {
        System.err.println("--->TestFastjsonHttpMessageConverter");
        return super.getResult(old);
    }
}
