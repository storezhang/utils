package com.ruijc.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简单的文件日志
 *
 * @author Storezhang
 */
public class FileLogger extends BaseLogger {

    private final DateFormat fileDateFormat;
    private final DateFormat logDateFormat;
    private String root;
    private PrintStream ps;
    private String date;

    public FileLogger(String root) {
        fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.root = root;
        File file = new File(root);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void main(String[] args) {
        FileLogger logger = new FileLogger("./log//");
        logger.log("test", "test", "", "success", true, "msg", "Test file input!");
    }

    @Override
    public void log(String store, String topic, String source, Object... args) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            if (0 == i) {
                content.append("|");
            }
            content.append(String.valueOf(args[i]))
                    .append("=")
                    .append(String.valueOf(args[++i]))
                    .append("|");
        }

        Date curDate = new Date(System.currentTimeMillis());
        String filename;
        String sCurDate = fileDateFormat.format(curDate);
        if (null == ps || !date.equals(sCurDate)) {
            date = sCurDate;
            filename = String.format("%s/%s.%s.log", root, store, date);
            try {
                ps = new PrintStream(new FileOutputStream(filename, true));
            } catch (FileNotFoundException e) {
                //Logger.log(e);
            }
        }
        if (null != ps) {
            ps.println(String.format("%s|%s|%s------>%s", logDateFormat.format(curDate), topic, source, content));
            ps.flush();
        }
    }
}
