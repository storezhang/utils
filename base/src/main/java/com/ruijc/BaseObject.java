/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc;

import com.alibaba.fastjson.JSON;

/**
 * 所有类的基类
 *
 * @author Storezhang
 */
public class BaseObject {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
