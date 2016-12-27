package com.ruijc.util;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

/**
 * 网络工具类
 *
 * @author Storezhang
 *         Create: 2016-12-28
 */

public class NetworkUtils {

    public static String realIp() {
        String realIp = "";

        String localIp = "";
        String netIp = "";

        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return realIp;
        }

        InetAddress ip;
        boolean found = false;
        while (netInterfaces.hasMoreElements() && !found) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    netIp = ip.getHostAddress();
                    found = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    localIp = ip.getHostAddress();
                }
            }
        }

        if (!StringUtils.isBlank(netIp)) {
            realIp = netIp;
        } else {
            realIp = localIp;
        }

        return realIp;
    }

    private static String netIp() {
        String ip;

        InputStream stream = null;
        try {
            URL url = new URL("http://1212.ip138.com/ic.asp");
            URLConnection conn = url.openConnection();
            stream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer webContent = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                webContent.append(line);
            }
            int ipStart = webContent.indexOf("[") + 1;
            int ipEnd = webContent.indexOf("]");
            ip = webContent.substring(ipStart, ipEnd);
        } catch (Exception e) {
            ip = "";
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    ip = "";
                }
            }
        }

        return ip;
    }

    public static void main(String[] args) {
        System.err.println("--->" + realIp() + ": " + netIp());
    }
}
