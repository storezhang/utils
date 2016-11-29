/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.http;

import com.alibaba.fastjson.JSON;
import com.ruijc.util.EncryptUtils;
import com.ruijc.util.MapUtils;
import com.ruijc.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Http工具类
 *
 * @author Storezhang
 */
public class HttpUtils {

    public static final String HTTP_PROTCOL = "http://";

    public static boolean checkSign(ServletRequest req, String signKey) {
        boolean ret;

        String sign = req.getParameter(signKey);
        if (StringUtils.isBlank(sign)) {
            ret = false;
            return ret;
        }

        Map<String, String> params = new HashMap<String, String>();

        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            params.put(name, req.getParameter(name));
        }
        params.put(signKey, "");
        String mySign = EncryptUtils.md5(MapUtils.linkString(params));

        ret = sign.equals(mySign);

        return ret;
    }

    /**
     * 向响应对象中输出字符串
     *
     * @param response 响应对象
     * @param content  内容
     * @throws IOException 异常
     */
    public static void write(ServletResponse response, String content) throws IOException {
        response.setContentLength(-1);
        PrintWriter writer = null;
        ServletOutputStream sos = null;
        try {
            writer = response.getWriter();
            writer.println(content);
        } catch (Exception e) {
            sos = response.getOutputStream();
            sos.println(content);
        } finally {
            if (null != writer) {
                writer.flush();
                writer.close();
            }
            if (null != sos) {
                sos.flush();
                sos.close();
            }
        }
    }

    /**
     * 向响应对象中输出字符串
     *
     * @param response 响应对象
     * @param data     数据
     * @throws IOException 异常
     */
    public static void write(ServletResponse response, Object data) throws IOException {
        response.setContentLength(-1);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String json = JSON.toJSONString(data);
        PrintWriter writer = null;
        ServletOutputStream sos = null;
        try {
            writer = response.getWriter();
            writer.println(json);
        } catch (Exception e) {
            sos = response.getOutputStream();
            sos.println(json);
        } finally {
            if (null != writer) {
                writer.flush();
                writer.close();
            }
            if (null != sos) {
                sos.flush();
                sos.close();
            }
        }
    }

    /**
     * 获得最终的地址（包括301或者302等跳转后的地址）
     *
     * @param url 原始地址
     * @return 最终的地址
     */
    public static String finalUrl(String url) {
        String to = url;
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl
                    .openConnection();
            conn.setConnectTimeout(5 * 60 * 1000);
            conn.setReadTimeout(5 * 60 * 1000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);

            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.addRequestProperty("Referer", "http://icoolxue.com/");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                to = conn.getHeaderField("Location");
                return finalUrl(to);
            } else {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return to;
    }

    public static String encode(String from) {
        return encode(from, "UTF-8");
    }

    /**
     * URL编码
     *
     * @param from    编码前的字符串
     * @param charset 编码
     * @return 编码后的字符串
     */
    public static String encode(String from, String charset) {
        String to;
        try {
            to = URLEncoder.encode(from, charset);
        } catch (UnsupportedEncodingException e) {
            to = from;
        }

        return to;
    }

    public static String decode(String from) {
        return decode(from, "UTF-8");
    }

    /**
     * URL解码
     *
     * @param from    解码前的字符串
     * @param charset 编码
     * @return 解码后的字符串
     */
    public static String decode(String from, String charset) {
        String to;
        try {
            to = URLDecoder.decode(from, charset);
        } catch (UnsupportedEncodingException e) {
            to = from;
        }

        return to;
    }

    /**
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，那么真
     * 正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * @param request 请求对象
     * @return 真实IP
     */
    public static String clientIP(ServletRequest request) {
        String ip = "127.0.0.1";
        if (!(request instanceof HttpServletRequest)) {
            return ip;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 取得客户端的Referer
     *
     * @param request 客户端请求
     * @return Referer
     */
    public static String getReferer(ServletRequest request) {
        String referer = "";

        if (!(request instanceof HttpServletRequest)) {
            return referer;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        referer = StringUtils.getString(req.getHeader("referer"), req.getHeader("Referer"));
        if (!StringUtils.isBlank(referer) && !referer.startsWith(HTTP_PROTCOL)) {
            referer += HTTP_PROTCOL;
        }

        return referer;
    }

    public static String getString(ServletRequest request) throws Exception {
        char[] readerBuffer = new char[request.getContentLength()];
        BufferedReader bufferedReader = request.getReader();

        int portion = bufferedReader.read(readerBuffer);
        int amount = portion;
        while (amount < readerBuffer.length) {
            portion = bufferedReader.read(readerBuffer, amount, readerBuffer.length - amount);
            amount = amount + portion;
        }

        StringBuilder stringBuffer = new StringBuilder((int) (readerBuffer.length * 1.5));
        for (int index = 0; index < readerBuffer.length; index++) {
            char c = readerBuffer[index];
            stringBuffer.append(c);
        }

        String xml = stringBuffer.toString();

        return xml;
    }
}
