/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.mybatis.cache.redis;

import com.ruijc.mybatis.cache.DummyReadWriteLock;
import com.ruijc.mybatis.cache.SerializerUtils;
import com.ruijc.util.serialize.ISerializer;
import org.apache.ibatis.cache.Cache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis的Redis缓存实现
 *
 * @author Storezhang
 */
public class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock;
    private final String id;
    private RedisTemplate<byte[], byte[]> redisTemplate;
    private ISerializer<Object> serializer;

    public RedisCache(final String id) {
        this.id = id;
        readWriteLock = new DummyReadWriteLock();
        redisTemplate = RedisUtils.getRedisTemplate();
        serializer = SerializerUtils.getSerializer();
    }

    public String getId() {
        return id;
    }

    public void putObject(final Object key, final Object value) {
        redisTemplate.opsForHash().put(id.getBytes(), key.toString().getBytes(), serializer.serialize(value));
    }

    public Object getObject(final Object key) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return serializer.deserialize(connection.hGet(id.getBytes(), key.toString().getBytes()));
            }
        });
    }

    public Object removeObject(final Object key) {
        return redisTemplate.opsForHash().delete(id.getBytes(), key);
    }

    public void clear() {
        redisTemplate.delete(id.getBytes());
    }

    public int getSize() {
        return redisTemplate.opsForHash().size(id.getBytes()).intValue();
    }

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
