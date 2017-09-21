/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import com.ruijc.Href;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 *
 * @author storezhang
 */
public class StringUtils {

    public static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    public static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
    private static final Pattern patternTag;
    private static final Pattern patternLink;
    private static Matcher matcherTag;
    private static Matcher matcherLink;

    static {
        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    public static String getNicknameFromEmail(String email) {
        int index = email.indexOf('@');
        if (-1 != index) {
            return email.substring(0, index);
        }
        return email;
    }

    /**
     * 从邮件地址从取得邮件服务器地址
     *
     * @param email 邮件地址
     * @return 邮件服务器地址
     */
    public static String emailServer(String email) {
        int index = email.indexOf('@');
        if (-1 != index) {
            return "http://mail." + email.substring(index + 1);
        }
        return email;
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        return result > 0.0000000000001;
    }

    /**
     * 判断是否为汉字
     *
     * @param str 字符串
     * @return 是否是汉字
     */
    public static boolean isGBK(String str) {
        char[] chars = str.toCharArray();
        boolean isGBK = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
                        && ints[1] <= 0xFE) {
                    isGBK = true;
                    break;
                }
            }
        }
        return isGBK;
    }

    public static String read(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line);
            content.append("\r\n");
        }
        return content.toString();
    }

    /**
     * 自定义参数解析注：此方法暂时没有考虑具有相同名称的一组提交值的情况，如需要，请自己更改下面的代码
     *
     * @param query 查询参数
     * @return 键值对
     */
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<String, String>();
        if (!isBlank(query)) {
            String[] tmps = query.split("&");
            if (null != tmps && tmps.length > 0) {
                for (String tmp : tmps) {
                    if (!isBlank(tmp)) {
                        String[] keyValue = tmp.split("=");
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return params;
    }

    /**
     * 删除字符串两边多余的空格
     *
     * @param str 字符串
     * @return 处理后的字符串
     */
    public static String delSpace(String str) {
        if (str == null) {
            return "";
        }
        String regStartSpace = "^[　 ]*";
        String regEndSpace = "[　 ]*$";
        return str.replaceAll(regStartSpace, "").replaceAll(regEndSpace, "");
    }

    public static String trimEx(String str) {
        if (str == null) {
            return "";
        }
        str = delSpace(str);

        return str.replaceAll("\\s{2,}", " ");
    }

    public static String domain(String url) {
        String domain = "";
        if (null == url || "".equals(url)) {
            return "";
        }
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        while (matcher.find()) {
            domain = matcher.group();
        }

        return domain;
    }

    /**
     * 子字符串方法
     *
     * @param from   原始字符串
     * @param len    需要的长度
     * @param append 添加字符串
     * @return 子字符串
     */
    public static String start(String from, int len, char append) {
        String key = "";

        if (null == from) {
            return key;
        }

        if (from.length() < len) {
            StringBuilder sb = new StringBuilder();
            sb.append(from);
            for (int i = 0; i < len - from.length(); ++i) {
                sb.append(append);
            }
            key = sb.toString();
        } else {
            key = from.substring(0, len);
        }

        return key;
    }

    /**
     * 子字符串方法
     *
     * @param from   原始字符串
     * @param len    需要的长度
     * @param append 添加字符串
     * @return 子字符串
     */
    public static String start(String from, int len, String append) {
        String ret;

        int strLen = from.length();
        if (strLen <= len) {
            ret = from.substring(0, strLen);
        } else {
            ret = from.substring(0, len) + append;
        }

        return ret;
    }

    /**
     * 子字符串方法
     *
     * @param from 原始字符串
     * @param len  需要的长度
     * @return 子字符串
     */
    public static String start(String from, int len) {
        return start(from, len, "...");
    }

    /**
     * 去除空白字符
     *
     * @param str 原始字符串
     * @return 去掉后的字符串
     */
    public static String replaceBlank(String str) {
        String dest = "";

        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }

    /**
     * 从字符串中获得列表字符串
     *
     * @param content 原始内容
     * @param space   分隔字符串
     * @return 字符串列表
     */
    public static String[] list(String content, String space) {
        if (null != content) {
            return content.split(space);
        }
        return new String[]{};
    }

    /**
     * 从字符串中获得列表字符串
     *
     * @param content 原始内容
     * @return 字符串列表
     */
    public static String[] list(String content) {
        return list(content, ",");
    }

    /**
     * 高级版的toString方法
     *
     * @param obj 对象
     * @return toString返回值
     */
    public static String toString(Object obj) {
        if (null == obj) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 删除字符串的HTML代码
     *
     * @param from 原始字符串
     * @return 删除HTML标签后的字符串
     */
    public static String clearHTML(String from) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(from);
        from = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(from);
        from = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(from);
        from = m_html.replaceAll(""); //过滤html标签

        return from.trim(); //返回文本字符串
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 结果
     */
    public static boolean isBlank(String str) {
        return null == str || str.isEmpty();
    }

    /**
     * 判断所有的字符串是不是或空
     *
     * @param strs 字符串列表
     * @return 是不是有一个为空
     */
    public static boolean isAnyBlank(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断所有的字符串是不是都为空
     *
     * @param strs 字符串列表
     * @return 是不是都为空
     */
    public static boolean isAllBlank(String... strs) {
        for (String str : strs) {
            if (!isBlank(str)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllEndsWith(String from, String... withs) {
        for (String with : withs) {
            if (!from.endsWith(with)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAnyEndsWith(String from, String... withs) {
        for (String with : withs) {
            if (from.endsWith(with)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将字符转换成Ascii字符
     *
     * @param from 原始字符串
     * @return Ascii字符串
     */
    public static String toAscii(String from) {
        StringBuilder sb = new StringBuilder();
        byte[] bt = from.getBytes();
        for (int i = 0; i < bt.length; i++) {
            if (bt[i] < 0) {//是汉字去高位1
                sb.append((char) (bt[i] & 0x7f));
            } else {//是英文字符补0作记录
                sb.append((char) 0);
                sb.append((char) bt[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 从Ascii字符串解码成原始字符串
     *
     * @param from 原始字符串
     * @return 转码后的字符串
     */
    public static String fromAscii(String from) {
        byte[] btAll = from.getBytes();
        int i, l = 0, length = btAll.length, j = 0;
        for (i = 0; i < length; i++) {
            if (btAll[i] == 0) {
                l++;
            }
        }

        byte[] btTrue = new byte[length - l];
        for (i = 0; i < length; i++) {
            if (btAll[i] == 0) {//是英文字符
                i++;

                btTrue[j] = btAll[i];
            } else {//是中文字符，高位补1
                btTrue[j] = (byte) (btAll[i] | 0x80);
            }
            j++;
        }

        String tt = new String(btTrue);
        return tt;
    }

    /**
     * 从字符串中取得所有的链接
     *
     * @param html 字符串
     * @return 链接列表
     */
    public static List<Href> getHrefs(String html) {
        List<Href> hrefs = new ArrayList<Href>();

        if (isBlank(html)) {
            return hrefs;
        }

        matcherTag = patternTag.matcher(html);
        while (matcherTag.find()) {
            String link = matcherTag.group(1);
            String text = matcherTag.group(2);
            matcherLink = patternLink.matcher(link);
            while (matcherLink.find()) {
                String url = matcherLink.group(1).replaceAll("\"", "").replaceAll("'", "");
                if (isBlank(url)) {
                    continue;
                }
                hrefs.add(new Href(url, text));
            }
        }

        return hrefs;
    }

    /**
     * 处理文字，将所有超链接按链接和文本添加到列表中
     *
     * @param text  包含链接的文本
     * @param links 装载链接的列表
     */
    public static void parseLink(String text, List<Map<String, String>> links) {
        List<Href> hrefs = StringUtils.getHrefs(text);
        if (null != hrefs && !hrefs.isEmpty()) {
            for (Href href : hrefs) {
                String link = href.getLink();
                if (isBlank(link)) {
                    continue;
                }
                if (-1 != link.indexOf("pan.baidu.com")) {
                    continue;
                }
                Map<String, String> url = new HashMap<String, String>();
                url.put("url", link);
                url.put("name", href.getText());
                links.add(url);
            }
        }
    }

    /**
     * 间隔插入字符串
     *
     * @param from     原始字符串
     * @param insert   要插入的字符串
     * @param interval 间隔
     * @return 处理后的字符串
     */
    public static String interval(String from, String insert, int interval) {
        StringBuilder ret = new StringBuilder();
        if (isBlank(from)) {
            return ret.toString();
        }

        int len = from.length();
        for (int index = 0; index < len; index += interval) {
            int end = index + interval;
            if (index >= len) {
                index = len - 1;
            }
            if (end >= len) {
                end = len - 1;
            }
            if (end == len - 1) {
                ret.append(from.subSequence(index, end));
            } else {
                ret.append(from.subSequence(index, end));
                ret.append(insert);
            }
        }

        return ret.toString();
    }

    /**
     * 间隔插入字符串
     *
     * @param from     原始字符串
     * @param interval 间隔
     * @return 处理后的字符串
     */
    public static String interval(String from, int interval) {
        return interval(from, "<br/>", interval);
    }

    /**
     * 间隔插入字符串
     *
     * @param from   原始字符串
     * @param insert 要插入的字符串
     * @return 处理后的字符串
     */
    public static String interval(String from, String insert) {
        return interval(from, insert, 30);
    }

    /**
     * 间隔插入字符串
     *
     * @param from 原始字符串
     * @return 处理后的字符串
     */
    public static String interval(String from) {
        return interval(from, "<br/>", 30);
    }

    public static boolean check(String check, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(check);
        return matcher.find();
    }

    public static boolean check(String check, String... patterns) {
        for (String patternString : patterns) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(check);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查字符串中是否包含另一个字符串
     *
     * @param target 要检查的目标字符串
     * @param test   要检查的字符串
     * @return 是否包含
     */
    public static boolean contains(String target, String test) {
        return contains(target, test, ",", ",");
    }

    /**
     * 检查字符串中是否包含另一个字符串
     *
     * @param target         要检查的目标字符串
     * @param test           要检查的字符串
     * @param targetInterval 目标字符串间隔字符
     * @param testInterval   要检查的字符串的间隔字符
     * @return 是否包含
     */
    public static boolean contains(String target, String test, String targetInterval, String testInterval) {
        if (isBlank(target)) {
            return false;
        }
        if (isBlank(test)) {
            return true;
        }

        String[] targetArray = target.split(targetInterval);
        if (null == targetArray || 0 == targetArray.length) {
            return false;
        }
        String[] testArray = test.split(testInterval);
        if (null == testArray || 0 == testArray.length) {
            return true;
        }

        for (String targetStr : targetArray) {
            for (String testStr : testArray) {
                if (!isBlank(targetStr) && !isBlank(testStr) && targetStr.equals(testStr)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 检查目标字符串里是不是包含所有给出的字符串
     *
     * @param content 目标字符串
     * @param checks  包含的所有字符串数组
     * @return 是否包含
     */
    public static boolean containsAll(String content, String... checks) {
        boolean contains = false;
        if (isBlank(content)) {
            return contains;
        }

        if (null == checks || 0 == checks.length) {
            contains = true;
            return contains;
        }

        for (String check : checks) {
            if (!content.contains(check)) {
                contains = false;
                break;
            }
        }

        return contains;
    }

    /**
     * 检查目标字符串里是不是包含任意给出的字符串
     *
     * @param content 目标字符串
     * @param checks  包含的任意字符串数组
     * @return 是否包含
     */
    public static boolean containsAny(String content, String... checks) {
        boolean contains = false;
        if (isBlank(content)) {
            return contains;
        }

        if (null == checks || 0 == checks.length) {
            contains = true;
            return contains;
        }

        for (String check : checks) {
            if (content.contains(check)) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * 从参数中取得字符串直到参数不为空
     *
     * @param params 参数
     * @return 最终字符串
     */
    public static String getString(String... params) {
        String ret = "";

        if (null == params || 0 == params.length) {
            return ret;
        }

        for (String param : params) {
            ret = param;
            if (!isBlank(ret)) {
                break;
            }
        }

        return ret;
    }

    /**
     * 检查字符串列表里是否包含指定的字符串组
     *
     * @param checks 要检查的字符串列表
     * @param tests  要验证的字符串组
     * @return 是否包含
     */
    public static boolean contains(List<String> checks, String... tests) {
        boolean contains = true;
        if (null == checks || checks.isEmpty()) {
            return contains;
        }

        for (String test : tests) {
            contains |= checks.contains(test);
        }

        return contains;
    }

    /**
     * 不区分大小写检查字符串列表
     *
     * @param checks 被检查的字符串列表
     * @param test   要检查的字符串
     * @return 是否在字符串列表中
     */
    public static boolean containsNCS(List<String> checks, String test) {
        boolean contains = false;
        if (null == checks || checks.isEmpty() || isBlank(test)) {
            return contains;
        }

        for (String check : checks) {
            if (check.toLowerCase().equals(test.toLowerCase())) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * 异常的堆栈字符串
     *
     * @param e 异常
     * @return 堆栈字符串
     */
    public static String stackTrace(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 连接字符串
     *
     * @param strings 字符串列表
     * @param contact 分隔符
     * @return 最终字符串
     */
    public static String contact(Collection<String> strings, String contact) {
        String ret = "";

        if (null == strings || strings.isEmpty()) {
            return ret;
        }

        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append(contact);
        }
        ret = sb.substring(0, sb.length() - contact.length());

        return ret;
    }

    /**
     * 从URL里提取文件名
     *
     * @param url 链接
     * @return 文件名
     */
    public static String getFilenameFromUrl(String url) {
        int lastNo = url.lastIndexOf("/");
        if (url.substring(lastNo).lastIndexOf(".") == -1) {
            return url;
        } else {
            if ("".equals(url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")))) {
                getFilenameFromUrl(url.substring(0, url.lastIndexOf("/")));
            } else {
                return url.substring(url.lastIndexOf("/") + 1);
            }
        }

        return url;
    }

    public static void main(String[] args) {
        //System.out.println("-->NickName: " + getNicknameFromEmail("storezhang@gmail.com"));
        //System.out.println(isMessyCode("springõûºïjdbc"));
        //System.out.println(isGBK("springõûºïjdbc"));
        //System.out.println(isMessyCode("你好"));
        //System.out.println(trimEx("  你   好   You  and  Me   !"));
        //System.out.println("--->" + getDomain("http://anotherbug.blog.chinajavaworld.com/entry/4545/0/"));
        //System.out.println("--->" + "*.icoolxue.com".replaceAll("\\*\\.", ""));
        //System.out.println("--->" + subString("I'm a chinese boy!", 3));
        //String ascii = toAscii("我爱你，中国！你真的好美！Yes");
        //System.out.println("--->" + ascii);
        //System.out.println("--->" + fromAscii(ascii));
        //System.err.println("--->" + getHrefs("<a href='www.icoolxue.com'>Href1</a>Content<a href='video.icoolxue.com'>Href2</a>"));
        System.err.println("--->" + interval("<a href='www.icoolxue.com'>Href1</a>Content<a href='video.icoolxue.com'>Href2</a>", "<br/>", 3));
    }
}
