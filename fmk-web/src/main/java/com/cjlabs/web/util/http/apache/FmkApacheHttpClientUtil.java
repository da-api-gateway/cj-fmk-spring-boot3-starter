package com.cjlabs.web.util.http.apache;

import com.cjlabs.web.util.http.FmkHttpConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class FmkApacheHttpClientUtil {
    @Getter
    private static volatile CloseableHttpClient httpClient;
    private static volatile FmkHttpConfig config;

    static {
        // 初始化默认配置
        config = new FmkHttpConfig();
        rebuildClient();
    }

    /**
     * 修改配置并重建 HttpClient
     */
    public static synchronized void updateConfig(FmkHttpConfig newConfig) {
        config = newConfig;

        // 如果配置了代理，则设置代理
        if (newConfig.isUseProxy()) {
            if (StringUtils.isNotBlank(newConfig.getProxyHost()) && newConfig.getProxyPort() > 0) {
                log.info("ApacheHttpClient configured with proxy: {}:{}", newConfig.getProxyHost(), newConfig.getProxyPort());
            }
        }

        rebuildClient();
        log.info("ApacheHttpClient rebuilt with new configuration");
    }

    private static void rebuildClient() {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getApache().getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(config.getApache().getMaxConnectionsPerRoute());

        // 创建请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.of(config.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS))
                .setResponseTimeout(Timeout.of(config.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS))
                .build();

        // 构建 HttpClient
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig);

        // 如果配置了代理，则设置代理
        if (config.isUseProxy() && StringUtils.isNotBlank(config.getProxyHost()) && config.getProxyPort() > 0) {
            HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort());
            builder.setProxy(proxy);
        }

        httpClient = builder.build();
    }

    /**
     * 创建响应处理器，处理响应并返回字符串结果
     */
    private static HttpClientResponseHandler<String> createResponseHandler(String methodName) {
        return response -> {
            int status = response.getCode();
            HttpEntity entity = response.getEntity();
            String responseBody = entity != null ? EntityUtils.toString(entity) : null;

            // 打印响应信息
            if (config.isPrintJson()) {
                log.info("ApacheHttpClientUtil|{}|Response Status={}", methodName, status);

                // 打印响应头
                if (config.isPrintJson()) {
                    Header[] headers = response.getHeaders();
                    if (headers != null && headers.length > 0) {
                        log.info("ApacheHttpClientUtil|{}|Response Headers:", methodName);
                        for (Header header : headers) {
                            log.info("ApacheHttpClientUtil|{}|{} = {}", methodName, header.getName(), header.getValue());
                        }
                    }
                }

                log.info("ApacheHttpClientUtil|{}|Response={}", methodName, responseBody);
            }

            return responseBody;
        };
    }

    // -------------------- GET 请求 --------------------
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, config.getRequestTimeout());
    }

    /**
     * 发送GET请求，支持自定义超时时间
     *
     * @param url     请求URL
     * @param headers 请求头
     * @param timeout 超时时间
     * @return 响应内容
     */
    public static String get(String url, Map<String, String> headers, Duration timeout) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        // 设置自定义超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.of(timeout.toMillis(), TimeUnit.MILLISECONDS))
                .build();
        httpGet.setConfig(requestConfig);

        // 添加请求头
        if (headers != null) {
            headers.forEach(httpGet::addHeader);
        }

        if (config.isPrintJson()) {
            log.info("ApacheHttpClientUtil|get|url={}", url);
            log.info("ApacheHttpClientUtil|get|timeout={}", timeout);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("ApacheHttpClientUtil|get|Headers: {}", headers);
            }
        }

        // 使用 HttpClientResponseHandler 处理响应
        return httpClient.execute(httpGet, createResponseHandler("get"));
    }

    // -------------------- POST JSON --------------------
    public static String postJson(String url, String json) throws IOException {
        return postJson(url, json, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        return postJson(url, json, headers, config.getRequestTimeout());
    }

    /**
     * 发送POST JSON请求，支持自定义超时时间
     *
     * @param url     请求URL
     * @param json    JSON请求体
     * @param headers 请求头
     * @param timeout 超时时间
     * @return 响应内容
     */
    public static String postJson(String url, String json, Map<String, String> headers, Duration timeout) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        // 设置自定义超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.of(timeout.toMillis(), TimeUnit.MILLISECONDS))
                .build();
        httpPost.setConfig(requestConfig);

        // 设置请求体
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        // 添加请求头
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }

        if (config.isPrintJson()) {
            log.info("ApacheHttpClientUtil|postJson|url={}", url);
            log.info("ApacheHttpClientUtil|postJson|timeout={}", timeout);
            log.info("ApacheHttpClientUtil|postJson|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("ApacheHttpClientUtil|postJson|Headers: {}", headers);
            }
        }

        // 使用 HttpClientResponseHandler 处理响应
        return httpClient.execute(httpPost, createResponseHandler("postJson"));
    }

    // -------------------- POST FORM --------------------
    public static String postForm(String url, Map<String, String> formData) throws IOException {
        return postForm(url, formData, null);
    }

    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers) throws IOException {
        return postForm(url, formData, headers, config.getRequestTimeout());
    }

    /**
     * 发送 POST Form 表单请求，支持自定义超时时间
     *
     * @param url      请求地址
     * @param formData form 数据，key=value
     * @param headers  额外请求头
     * @param timeout  超时时间
     * @return 响应内容
     */
    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers, Duration timeout) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        // 设置自定义超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.of(timeout.toMillis(), TimeUnit.MILLISECONDS))
                .build();
        httpPost.setConfig(requestConfig);

        // 构建 form 编码字符串
        StringBuilder sb = new StringBuilder();
        if (formData != null && !formData.isEmpty()) {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        String formBody = sb.toString();
        StringEntity stringEntity = new StringEntity(formBody, ContentType.APPLICATION_FORM_URLENCODED);
        httpPost.setEntity(stringEntity);

        // 添加请求头
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }

        if (config.isPrintJson()) {
            log.info("ApacheHttpClientUtil|postForm|url={}", url);
            log.info("ApacheHttpClientUtil|postForm|timeout={}", timeout);
            log.info("ApacheHttpClientUtil|postForm|formData={}", formBody);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("ApacheHttpClientUtil|postForm|Headers: {}", headers);
            }
        }

        // 使用 HttpClientResponseHandler 处理响应
        return httpClient.execute(httpPost, createResponseHandler("postForm"));
    }
}