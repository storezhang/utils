/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import com.ruijc.Page;

import java.util.List;

/**
 * 分页工具类
 *
 * @author storezhang
 */
public class PageUtils {

    /**
     * 计算开始下标，结束下标
     *
     * @param pageNum  页数
     * @param pageSize 每页显示个数
     * @return 值
     */
    public static int calIndex(int pageNum, int pageSize) {
        int startIndex;
        startIndex = (pageNum - 1) * pageSize;
        if (startIndex < 0) {
            startIndex = 0;
        }
        return startIndex;
    }

    /**
     * 返回通用分页对象
     *
     * @param <T>      类型
     * @param t        对象
     * @param total    对象总数
     * @param pageNum  当前页
     * @param pageSize 每页显示数目
     * @return 分页对象
     */
    public static <T> Page<T> getPage(List<T> t, long total, int pageNum, int pageSize) {
        Page<T> page = new Page<T>();
        long totalPage = ((total - 1) / pageSize) + 1;
        long next = pageNum + 1;
        long previous = pageNum - 1;
        if (next >= totalPage) {
            next = totalPage;
        }
        if (previous <= 1) {
            previous = 1;
        }
        page.setPrevious(previous);
        page.setCurrent(pageNum);
        page.setNext(next);
        page.setTotal(totalPage);
        page.setData(t);
        page.setHit(total);
        return page;
    }
}
