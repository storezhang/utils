/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.io.*;

/**
 * 进程工具类
 *
 * @author Storezhang
 */
public class ProgressUtils {

    /**
     * 打印进程输出
     *
     * @param process 进程
     * @param out     标准输出
     * @param err     错误输出
     */
    public static void read(final Process process, final PrintStream out, final PrintStream err) {
        read(process.getInputStream(), out);
        read(process.getErrorStream(), err);
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     * @param out     标准输出
     */
    public static void read(final Process process, final PrintStream out) {
        read(process.getInputStream(), out);
    }

    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * 检测程序是否存在
     *
     * @param processName 线程的名字，请使用准确的名字
     * @return 进程是否存在
     */
    public static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName + '"');
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    public static void main(String[] args) {
        System.err.println("--->" + findProcess("netbeans.exe"));
    }
}
