/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.mybatis.cache.redis;

import com.ruijc.mybatis.cache.DummyReadWriteLock;
import com.ruijc.mybatis.cache.SerializerUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis的Redis缓存实现
 *
 * @author Storezhang
 */
public class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock;
    private final String id;

    public RedisCache(final String id) {
        this.id = id;
        readWriteLock = new DummyReadWriteLock();
    }

    public String getId() {
        return id;
    }

    public void putObject(final Object key, final Object value) {
        RedisUtils.getRedisTemplate().execute(new RedisCallback<Void>() {
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hSet(id.getBytes(), key.toString().getBytes(), SerializerUtils.getSerializer().serialize(value));
                connection.expire(id.getBytes(), RedisUtils.getRedisProperties().getExpire());

                return null;
            }
        });
    }

    public Object getObject(final Object key) {
        return RedisUtils.getRedisTemplate().execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return SerializerUtils.getSerializer().deserialize(connection.hGet(id.getBytes(), key.toString().getBytes()));
            }
        });
    }

    public Object removeObject(final Object key) {
        return RedisUtils.getRedisTemplate().execute(new RedisCallback<Void>() {
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hDel(id.getBytes(), key.toString().getBytes());

                return null;
            }
        });
    }

    public void clear() {
        RedisUtils.getRedisTemplate().execute(new RedisCallback<Void>() {
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(id.getBytes());

                return null;
            }
        });
    }

    public int getSize() {
        return RedisUtils.getRedisTemplate().opsForHash().size(id.getBytes()).intValue();
    }

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
