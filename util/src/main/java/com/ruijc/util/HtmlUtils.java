/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

/**
 * HTML工具类
 *
 * @author Storezhang
 */
public class HtmlUtils {

    public static String fill(String from) {
        return fillBody(from);
    }

    public static String fillBody(String from) {
        return String.format("<html><body>%s</body></html>", from);
    }
}
