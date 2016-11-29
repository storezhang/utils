/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util.serialize;

/**
 * 序列化接口
 *
 * @author Storezhang
 */
public interface ISerializer<T> {

    public <T> T deserialize(byte[] bytes);

    public byte[] serialize(Object object);
}
