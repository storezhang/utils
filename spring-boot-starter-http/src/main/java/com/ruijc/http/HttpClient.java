package com.ruijc.http;

import com.ruijc.util.CollectionUtils;
import com.ruijc.util.RandomUtils;
import com.ruijc.util.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http客户端
 *
 * @author storezhang
 */
public class HttpClient {

    public static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
    public static final int DEFAULT_SO_TIMEOUT = 3000;
    public static final String HTTP_PRO = "http://";
    public static final String HTTPS_PRO = "http://";
    private static HttpClient self;
    private org.apache.http.client.HttpClient client;
    private CookieStore cookieStore;
    private RequestConfig config;
    private int connectTimeout;
    private int soTimeout;
    private List<HttpHost> proxies;

    protected HttpClient() {
        proxies = new ArrayList<HttpHost>();

        HttpClientBuilder builder = HttpClientBuilder.create();

        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(0);
            return;
        }

        builder.setSSLContext(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(500);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        builder.setConnectionManager(connMgr);

        config = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        builder.setDefaultRequestConfig(config);

        cookieStore = new BasicCookieStore();
        builder.setDefaultCookieStore(cookieStore);
        client = builder.build();

        connectTimeout = DEFAULT_CONNECTION_TIMEOUT;
        soTimeout = DEFAULT_SO_TIMEOUT;
    }

    public static synchronized HttpClient instance() {
        if (null == self) {
            self = new HttpClient();
        }

        return self;
    }

    public static void main(String[] args) {
        HttpClient client = HttpClient.instance();
        client.downloadFile("http://www.zhuanpaopao.com/welcome/verifyCode", "code.jpg");
        String code = client.post("http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=zhuanpaopao", null, null, "", "img", new File("code.jpg"));
        System.err.println("--->" + code);
    }

    public void addProxy(String url, int port) {
        HttpHost proxy = new HttpHost(url, port);
        proxies.add(proxy);
    }

    public void setConnectionTimeout(int timeout) {
        connectTimeout = timeout;
    }

    public void setSoTimeout(int timeout) {
        soTimeout = timeout;
    }

    public void clearTimeout() {
        connectTimeout = DEFAULT_CONNECTION_TIMEOUT;
        soTimeout = DEFAULT_SO_TIMEOUT;
    }

    public String getJSessionId() {
        List<Cookie> cookies = cookieStore.getCookies();
        String sessionId;
        for (Cookie cookie : cookies) {
            if ("jsessionid".equalsIgnoreCase(cookie.getName())) {
                sessionId = cookie.getValue();
                return sessionId;
            }
        }
        return "";
    }

    public String getCookie(String key) {
        String ret = "";

        List<Cookie> cookies = cookieStore.getCookies();
        if (null == cookies || cookies.isEmpty()) {
            return ret;
        }

        for (Cookie cookie : cookies) {
            if (key.equalsIgnoreCase(cookie.getName())) {
                ret = cookie.getValue();
                break;
            }
        }

        return ret;
    }

    public int getCookieSize() {
        int size = 0;

        List<Cookie> cookies = cookieStore.getCookies();
        if (null != cookies && !cookies.isEmpty()) {
            size = cookies.size();
        }

        return size;
    }

    public void clearCookies() {
        cookieStore.clear();
    }

    public void setCookies(Map<String, String> cookies, String domain) {
        setCookies(cookies, domain, "");
    }

    public void setCookies(Map<String, String> cookies, String domain, String path) {
        if (null == cookies || cookies.isEmpty()) {
            return;
        }

        for (String key : cookies.keySet()) {
            BasicClientCookie cookie = new BasicClientCookie(key, cookies.get(key));
            if (domain.startsWith(HTTP_PRO)) {
                domain = domain.substring(HTTP_PRO.length());
            }
            if (domain.startsWith(HTTPS_PRO)) {
                domain = domain.substring(HTTPS_PRO.length());
            }
            cookie.setDomain(domain);
            if (StringUtils.isBlank(path)) {
                cookie.setPath("/");
            } else {
                cookie.setPath(path);
            }
            cookieStore.addCookie(cookie);
        }
    }

    protected void setHeaders(HttpRequestBase req, Map<String, String> headers) {
        if (null != headers) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                req.setHeader(key, headers.get(key));
            }
        }
    }

    public String post(String url) {
        return post(url, null, null, "", "", null);
    }

    public String post(String url, String referer) {
        return post(url, null, null, referer, "", null);
    }

    public String post(String url, Map<String, String> params) {
        return post(url, params, null, url, "", null);
    }

    public String post(String url, Map<String, String> params, Map<String, String> headers) {
        return post(url, params, headers, "", "", null);
    }

    public String post(String url, Map<String, String> params, Map<String, String> headers, String referer, String fileParam, File file) {
        HttpPost post = postForm(url, params, fileParam, file);
        setReferer(post, referer);
        setHeaders(post, headers);

        return invoke(client, post);
    }

    public String postData(String url, String data, Map<String, String> headers, String charset) {
        return postData(url, data, headers, "", charset);
    }

    public String postData(String url, String data, Map<String, String> headers, String referer, String charset) {
        HttpPost post = postData(url, data);
        setReferer(post, referer);
        setHeaders(post, headers);

        return invoke(client, post, charset);
    }

    public String get(String url) {
        return get(url, "", null);
    }

    public String get(String url, String referer) {
        return get(url, referer, null);
    }

    public String get(String url, String referer, Map<String, String> headers) {
        HttpGet get = new HttpGet(url);
        setReferer(get, referer);
        setHeaders(get, headers);

        return invoke(client, get);
    }

    public String delete(String url) {
        return delete(url, "", null);
    }

    public String delete(String url, String referer) {
        return delete(url, referer, null);
    }

    public String delete(String url, String referer, Map<String, String> headers) {
        HttpDelete delete = new HttpDelete(url);
        setReferer(delete, referer);
        setHeaders(delete, headers);

        return invoke(client, delete);
    }

    public String put(String url) {
        return put(url, null, null, "");
    }

    public String put(String url, String referer) {
        return put(url, null, null, referer);
    }

    public String put(String url, Map<String, String> params) {
        return put(url, params, null, "");
    }

    public String put(String url, Map<String, String> params, Map<String, String> headers) {
        return put(url, params, headers, "");
    }

    public String put(String url, Map<String, String> params, Map<String, String> headers, String referer) {
        HttpPut put = putForm(url, params);
        setReferer(put, referer);
        setHeaders(put, headers);

        return invoke(client, put);
    }

    protected void setReferer(HttpRequestBase req, String referer) {
        req.setHeader("Referer", referer);
    }

    protected String invoke(org.apache.http.client.HttpClient client, HttpRequestBase request) {
        return invoke(client, request, "UTF-8");
    }

    protected String invoke(org.apache.http.client.HttpClient client, HttpRequestBase request, String charset) {
        HttpResponse response = sendRequest(client, request);
        String body = paseResponse(response, charset);

        return body;
    }

    protected String paseResponse(HttpResponse response, String charset) {
        String body = "";

        if (null == response) {
            return body;
        }

        HttpEntity entity = response.getEntity();
        try {
            body = EntityUtils.toString(entity, charset);
        } catch (ParseException e) {
            body = "";
        } catch (IOException e) {
            body = "";
        }

        return body;
    }

    private HttpResponse sendRequest(org.apache.http.client.HttpClient httpClient, HttpRequestBase request) {
        HttpResponse response = null;

        RequestConfig.Builder build = RequestConfig.copy(config);
        if (0 != soTimeout) {
            build.setSocketTimeout(soTimeout);
        }
        if (0 != connectTimeout) {
            build.setConnectTimeout(connectTimeout);
        }
        if (!CollectionUtils.isBlank(proxies)) {
            build.setProxy(RandomUtils.random(proxies));
        }
        request.setConfig(build.build());

        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            return response;
        }

        return response;
    }

    private HttpPost postForm(String url, Map<String, String> params, String fileParam, File file) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (null != params) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                builder.addTextBody(key, params.get(key), ContentType.DEFAULT_BINARY);
            }
        }
        if (!StringUtils.isBlank(fileParam) && null != file) {
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody(fileParam, file, ContentType.DEFAULT_BINARY, fileParam);
        }
        httpPost.setEntity(builder.build());

        return httpPost;
    }

    private HttpPost postData(String url, String data) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(data, "UTF-8"));

        return httpPost;
    }

    private HttpPut putForm(String url, Map<String, String> params) {
        HttpPut httpPut = new HttpPut(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpPut.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return httpPut;
        }

        return httpPut;
    }

    public String content(String server) {
        URL url;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader lineReader = null;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(server);
            is = url.openStream();
            isr = new InputStreamReader(is, "UTF-8");
            lineReader = new BufferedReader(isr);
            String line;
            while (null != (line = lineReader.readLine())) {
                result.append(line);
            }
        } catch (MalformedURLException ex) {
            return "";
        } catch (IOException ex) {
            return "";
        } finally {
            try {
                if (null != lineReader) {
                    lineReader.close();
                }
                if (null != isr) {
                    isr.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                return "";
            }
        }

        return result.toString();
    }

    public boolean downloadFile(String url, String savePath) {
        boolean success;

        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            int state = response.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream input = entity.getContent();
                FileOutputStream bout = new FileOutputStream(savePath);

                byte[] buffer = new byte[1024 * 100000];
                int read;

                while ((read = input.read(buffer)) > 0) {
                    bout.write(buffer, 0, read);
                    System.out.println(read);
                }
                input.close();
                bout.close();

                success = true;
            } else {
                success = false;
            }
        } catch (ClientProtocolException e) {
            success = false;
        } catch (IOException e) {
            success = false;
        }

        return success;
    }
}
