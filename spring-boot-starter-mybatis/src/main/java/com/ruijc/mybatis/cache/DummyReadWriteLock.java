/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.mybatis.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Mybatis的Redis缓存动态读写锁
 *
 * @author Storezhang
 */
public class DummyReadWriteLock implements ReadWriteLock {

    private final Lock lock;

    public DummyReadWriteLock() {
        lock = new DummyLock();
    }

    public Lock readLock() {
        return lock;
    }

    public Lock writeLock() {
        return lock;
    }

    static class DummyLock implements Lock {

        public void lock() {
            System.err.println("--->没有具体实现！");
        }

        public void lockInterruptibly() throws InterruptedException {
            System.err.println("--->没有具体实现！");
        }

        public boolean tryLock() {
            return true;
        }

        public boolean tryLock(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
            return true;
        }

        public void unlock() {
            System.err.println("--->没有具体实现！");
        }

        public Condition newCondition() {
            return null;
        }
    }
}
