package com.ruijc.log;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.request.PutLogsRequest;
import com.aliyun.openservices.log.response.GetLogsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用阿里云的SLS框架的日志记录器
 *
 * @author Storezhang
 */
public class AliyunSLSLogger extends BaseLogger {

    private final String project;
    private final Client client;

    public AliyunSLSLogger(String key, String secret, String endPoint, String project, String source) {
        this.project = project;
        client = new Client(endPoint, key, secret, source, true);
    }

    @Override
    public void log(String store, String topic, String source, Object... args) {
        List<LogItem> logs = new ArrayList<LogItem>();

        LogItem item = new LogItem((int) (System.currentTimeMillis() / 1000));
        for (int i = 0; i < args.length; ++i) {
            item.PushBack(String.valueOf(args[i]), String.valueOf(args[++i]));
        }
        logs.add(item);

        PutLogsRequest req = new PutLogsRequest(project, store, topic, source, logs);
        try {
            client.PutLogs(req);
        } catch (LogException ex) {
            //Logger.log(ex);
        }
    }

    @Override
    public List<Map<String, Object>> query(String store, String topic, long start, long end, String query, long offset, int num) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        GetLogsRequest req = new GetLogsRequest(
                project,
                store,
                (int) (start / 1000),
                (int) (end / 1000),
                topic,
                query,
                (int) offset,
                num,
                true
        );
        GetLogsResponse res;
        try {
            res = client.GetLogs(req);
        } catch (Exception e) {
            //Logger.log(e);
            return data;
        }

        List<QueriedLog> logs = res.GetLogs();
        if (null != logs && !logs.isEmpty()) {
            for (QueriedLog log : logs) {
                Map<String, Object> logData = new HashMap<String, Object>();
                logData.put("source", log.GetSource());
                List<LogContent> contents = log.GetLogItem().GetLogContents();
                if (null != contents && !contents.isEmpty()) {
                    for (LogContent content : contents) {
                        logData.put(content.GetKey(), content.GetValue());
                    }
                }
                data.add(logData);
            }
        }

        return data;
    }
}
