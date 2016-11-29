/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc;

import java.util.List;

/**
 * 分页对象
 *
 * @param <T> 类型
 * @author storezhang
 */
public class Page<T> extends BaseObject {

    public static final int PAGE_NUMBER = 1;//默认显示页数
    public static final int PAGE_SIZE = 12;//默认每页显示数
    private long previous;//前一页页数
    private long current;//当前页数
    private long next;//后一页页数
    private long total;//总页数
    private long hit;//总共有多少条数据
    private List<T> data;//数据列表

    public long getPrevious() {
        return previous;
    }

    public void setPrevious(long previous) {
        this.previous = previous;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getNext() {
        return next;
    }

    public void setNext(long next) {
        this.next = next;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getHit() {
        return hit;
    }

    public void setHit(long hit) {
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "Page{" + "previous=" + previous + ", next=" + next + ", total=" + total + ", data=" + data + '}';
    }
}
