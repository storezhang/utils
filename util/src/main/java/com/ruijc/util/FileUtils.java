/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件工具类
 *
 * @author Storezhang
 */
public class FileUtils {

    public static final long KB = 1024;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;

    /**
     * 获得临时文件名
     *
     * @param filename 文件名
     * @return 临时文件名
     */
    public static String tmpFilename(String filename) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        return tmpDir + "/" + filename;
    }

    /**
     * 生产文件如果文件所在路径不存在则生成路径
     *
     * @param fileName    文件名 带路径
     * @param isDirectory 是否为路径
     * @return 生气的文件对象
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    /**
     * Java文件操作 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     * @author Storezhang
     */
    public static String getExt(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            } else if (-1 == dot) {
                return "";
            }
        }

        return filename;
    }

    /**
     * Java文件操作 获取文件扩展名
     *
     * @param file 文件
     * @return 扩展名
     * @author Storezhang
     */
    public static String getExt(File file) {
        return getExt(file.getName());
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filename 文件名
     * @return 文件名（不带扩展名）
     */
    public static String getNameWithoutExt(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }

        return filename;
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param file 文件
     * @return 文件名（不带扩展名）
     */
    public static String getNameWithoutExt(File file) {
        return getNameWithoutExt(file.getName());
    }

    /**
     * 重命名文件
     *
     * @param file 原文件
     * @param name 新名字
     * @return 重命名后的文件
     */
    public static File rename(File file, String name) {
        String dir = file.getParentFile().getAbsolutePath();
        String ext = getExt(name);
        if (StringUtils.isBlank(ext)) {
            name = name + "." + getExt(file.getName());
        }

        File newFile = new File(dir + "/" + name);
        if (file.renameTo(newFile)) {
            return newFile;
        } else {
            return null;
        }
    }

    public static File[] listFilesByName(File path) {
        File[] files = path.listFiles();

        sortFilesByName(files);

        return files;
    }

    public static File[] listFilesByName(File path, FilenameFilter filter) {
        File[] files = path.listFiles(filter);

        sortFilesByName(files);

        return files;
    }

    private static void sortFilesByName(File[] files) {
        Arrays.sort(files, new WindowsExplorerComparator());
    }

    public static void main(String[] args) {
        rename(new File("./test.txt"), "new.mp4");
    }

    public static class WindowsExplorerComparator implements Comparator<File> {

        private String str1, str2;
        private int pos1, pos2, len1, len2;

        public int compare(File f1, File f2) {
            str1 = f1.getName();
            str2 = f2.getName();
            len1 = str1.length();
            len2 = str2.length();
            pos1 = pos2 = 0;

            int result = 0;
            while (result == 0 && pos1 < len1 && pos2 < len2) {
                char ch1 = str1.charAt(pos1);
                char ch2 = str2.charAt(pos2);

                if (Character.isDigit(ch1)) {
                    result = Character.isDigit(ch2) ? compareNumbers() : -1;
                } else if (Character.isLetter(ch1)) {
                    result = Character.isLetter(ch2) ? compareOther(true) : 1;
                } else {
                    result = Character.isDigit(ch2) ? 1 : Character.isLetter(ch2) ? -1 : compareOther(false);
                }

                pos1++;
                pos2++;
            }

            return result == 0 ? len1 - len2 : result;
        }

        private int compareNumbers() {
            int end1 = pos1 + 1;
            while (end1 < len1 && Character.isDigit(str1.charAt(end1))) {
                end1++;
            }
            int fullLen1 = end1 - pos1;
            while (pos1 < end1 && str1.charAt(pos1) == '0') {
                pos1++;
            }

            int end2 = pos2 + 1;
            while (end2 < len2 && Character.isDigit(str2.charAt(end2))) {
                end2++;
            }
            int fullLen2 = end2 - pos2;
            while (pos2 < end2 && str2.charAt(pos2) == '0') {
                pos2++;
            }

            int delta = (end1 - pos1) - (end2 - pos2);
            if (delta != 0) {
                return delta;
            }

            while (pos1 < end1 && pos2 < end2) {
                delta = str1.charAt(pos1++) - str2.charAt(pos2++);
                if (delta != 0) {
                    return delta;
                }
            }

            pos1--;
            pos2--;

            return fullLen2 - fullLen1;
        }

        private int compareOther(boolean isLetters) {
            char ch1 = str1.charAt(pos1);
            char ch2 = str2.charAt(pos2);

            if (ch1 == ch2) {
                return 0;
            }

            if (isLetters) {
                ch1 = Character.toUpperCase(ch1);
                ch2 = Character.toUpperCase(ch2);
                if (ch1 != ch2) {
                    ch1 = Character.toLowerCase(ch1);
                    ch2 = Character.toLowerCase(ch2);
                }
            }

            return ch1 - ch2;
        }
    }
}
