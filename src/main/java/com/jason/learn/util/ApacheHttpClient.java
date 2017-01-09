package com.jason.learn.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jason on 2016/12/6.
 */
public class ApacheHttpClient {
    private static final PoolingHttpClientConnectionManager cm;
    private static final ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
    private static final RequestConfig requestConfig;
    private static final String EMPTY_STR = "";
    private static final String UTF_8 = "UTF-8";
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;
    private static final int REQUEST_TIMEOUT = 1000;

    /* 从连接池中取连接的超时时间 */
    private static final int REQUEST_CONN_TIMEOUT = 5;


    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);// 整个连接池最大连接数
        cm.setDefaultMaxPerRoute(5);// 每路由最大连接数

        requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).
                setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(REQUEST_TIMEOUT).build();

        connectionKeepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    //如果服务器没有设置keep-alive这个参数，我们就把它设置成5秒
                    keepAlive = 5000;
                }
                return keepAlive;
            }

        };
    }

    /**
     * 通过连接池获取HttpClient
     * @param isSecure : 是否是SSL协议
     * @return
     */
    private static CloseableHttpClient getHttpClient(boolean isSecure) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                                .setConnectionManager(cm)
                                .setDefaultRequestConfig(requestConfig)
                                .setKeepAliveStrategy(connectionKeepAliveStrategy);
        if(isSecure) {
            try {
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

                httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
            }catch (NoSuchAlgorithmException | KeyManagementException |KeyStoreException e) {
                e.printStackTrace();
            }
        }


        return httpClientBuilder.build();
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url, boolean isSecure) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet, isSecure);
    }

    public static String httpGetRequest(String url, boolean isSecure,  Map<String, Object> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet, isSecure);
    }

    public static String httpGetRequest(String url, boolean isSecure, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet, isSecure);
    }

    public static String httpPostRequest(String url, boolean isSecure) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost, isSecure);
    }

    public static String httpPostRequest(String url, boolean isSecure, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost, isSecure);
    }

    public static String httpPostRequest(String url, boolean isSecure, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost, isSecure);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request, boolean isSecure) {
        CloseableHttpClient httpClient = getHttpClient(isSecure);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity);
                response.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return EMPTY_STR;
    }

    static public PoolStats getPoolStatus(){
        return cm.getTotalStats();
    }
}