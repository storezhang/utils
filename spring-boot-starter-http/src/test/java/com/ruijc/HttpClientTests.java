package com.ruijc;

import com.ruijc.http.HttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试用例
 *
 * @author Storezhang
 */
public class HttpClientTests {

    private HttpClient client;

    @Before
    public void init() {
        client = HttpClient.instance();
    }

    @Test
    public void testPost() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "add_msg");
        params.put("msg", "测试一下");
        client.addProxy("127.0.0.1", 8888);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; utf-8");
        String ret = client.post("http://www.shen-qi.com/webdo/indexdo.php", params, headers, "http://www.shen-qi.com/index.php", "", null);
        //Assert.assertEquals("addmsgok", ret);
    }
}
