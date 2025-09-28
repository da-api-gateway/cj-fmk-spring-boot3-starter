package com.cjlabs.core.http.jdk21;

import com.cjlabs.core.http.FmkHttpConfig;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FmkJdkHttpClientUtil {
    // 默认 logger，类名就是日志的名字
    private static final Logger log = LoggerFactory.getLogger(FmkJdkHttpClientUtil.class);

    private static volatile HttpClient client;
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
                ProxySelector proxySelector = ProxySelector.of(
                        new InetSocketAddress(newConfig.getProxyHost(), newConfig.getProxyPort())
                );
                newConfig.setProxySelector(proxySelector);
                log.info("JdkHttpClient configured with proxy: {}:{}", newConfig.getProxyHost(), newConfig.getProxyPort());
            } else {
                newConfig.setProxySelector(ProxySelector.getDefault());
            }
        }

        rebuildClient();
        log.info("HttpClient rebuilt with new configuration");
    }

    private static void rebuildClient() {
        client = HttpClient.newBuilder()
                .connectTimeout(config.getConnectTimeout())
                .proxy(config.getProxySelector())
                .version(config.getJdk().getHttpVersion().getClientVersion())
                .build();
    }

    /**
     * 打印响应头信息
     */
    private static void logResponseHeaders(HttpResponse<?> response, String methodName) {
        if (config.isPrintJson()) {
            Map<String, List<String>> headers = response.headers().map();
            if (MapUtils.isNotEmpty(headers)) {
                log.info("HttpClientUtil|{}|Response Headers:", methodName);
                headers.forEach((name, values) -> {
                    log.info("HttpClientUtil|{}|{} = {}", methodName, name, String.join(", ", values));
                });
            }
        }
    }

    // -------------------- GET 请求 --------------------
    public static String get(String url) throws IOException, InterruptedException {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException, InterruptedException {
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
    public static String get(String url, Map<String, String> headers, Duration timeout) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        if (config.isPrintJson()) {
            log.info("HttpClientUtil|get|url={}", url);
            log.info("HttpClientUtil|get|timeout={}", timeout);
            if (headers != null && !headers.isEmpty()) {
                log.info("HttpClientUtil|get|Headers: {}", headers);
            }
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // 打印响应信息
        if (config.isPrintJson()) {
            log.info("HttpClientUtil|get|Response Status={}", response.statusCode());

            // 打印响应头
            logResponseHeaders(response, "get");

            log.info("HttpClientUtil|get|Response={}", responseBody);
        }

        return responseBody;
    }

    /**
     * 异步发送GET请求
     *
     * @param url     请求URL
     * @param headers 请求头
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> getAsync(String url, Map<String, String> headers) {
        return getAsync(url, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送GET请求，支持自定义超时时间
     *
     * @param url     请求URL
     * @param headers 请求头
     * @param timeout 超时时间
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> getAsync(String url, Map<String, String> headers, Duration timeout) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        if (config.isPrintJson()) {
            log.info("HttpClientUtil|getAsync|url={}", url);
            log.info("HttpClientUtil|getAsync|timeout={}", timeout);
            if (headers != null && !headers.isEmpty()) {
                log.info("HttpClientUtil|getAsync|Headers: {}", headers);
            }
        }

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    // 打印响应信息
                    if (config.isPrintJson()) {
                        log.info("HttpClientUtil|getAsync|Response Status={}", response.statusCode());
                        logResponseHeaders(response, "getAsync");
                        log.info("HttpClientUtil|getAsync|Response={}", response.body());
                    }
                    return response.body();
                });
    }

    // -------------------- POST JSON --------------------
    public static String postJson(String url, String json) throws IOException, InterruptedException {
        return postJson(url, json, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException, InterruptedException {
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
    public static String postJson(String url, String json, Map<String, String> headers, Duration timeout) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postJson|url={}", url);
            log.info("HttpClientUtil|postJson|timeout={}", timeout);
            log.info("HttpClientUtil|postJson|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("HttpClientUtil|postJson|Headers: {}", headers);
            }
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // 打印响应信息
        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postJson|Response Status={}", response.statusCode());

            // 打印响应头
            logResponseHeaders(response, "postJson");

            log.info("HttpClientUtil|postJson|Response={}", responseBody);
        }

        return responseBody;
    }

    /**
     * 异步发送POST JSON请求
     *
     * @param url     请求URL
     * @param json    JSON请求体
     * @param headers 请求头
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> postJsonAsync(String url, String json, Map<String, String> headers) {
        return postJsonAsync(url, json, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送POST JSON请求，支持自定义超时时间
     *
     * @param url     请求URL
     * @param json    JSON请求体
     * @param headers 请求头
     * @param timeout 超时时间
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> postJsonAsync(String url, String json, Map<String, String> headers, Duration timeout) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postJsonAsync|url={}", url);
            log.info("HttpClientUtil|postJsonAsync|timeout={}", timeout);
            log.info("HttpClientUtil|postJsonAsync|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("HttpClientUtil|postJsonAsync|Headers: {}", headers);
            }
        }

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    // 打印响应信息
                    if (config.isPrintJson()) {
                        log.info("HttpClientUtil|postJsonAsync|Response Status={}", response.statusCode());
                        logResponseHeaders(response, "postJsonAsync");
                        log.info("HttpClientUtil|postJsonAsync|Response={}", response.body());
                    }
                    return response.body();
                });
    }

    // -------------------- POST FORM --------------------
    public static String postForm(String url, Map<String, String> formData) throws IOException, InterruptedException {
        return postForm(url, formData, null);
    }

    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers) throws IOException, InterruptedException {
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
    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers, Duration timeout) throws IOException, InterruptedException {
        // 构建 form 编码字符串
        StringBuilder sb = new StringBuilder();
        if (formData != null && !formData.isEmpty()) {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (!sb.isEmpty()) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        String formBody = sb.toString();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formBody));

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        // 打印请求信息
        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postForm|url={}", url);
            log.info("HttpClientUtil|postForm|timeout={}", timeout);
            log.info("HttpClientUtil|postForm|formData={}", formBody);
            if (headers != null && !headers.isEmpty()) {
                log.info("HttpClientUtil|postForm|Headers={}", headers);
            }
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // 打印响应信息
        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postForm|Response Status={}", response.statusCode());

            // 打印响应头
            logResponseHeaders(response, "postForm");

            log.info("HttpClientUtil|postForm|Response={}", responseBody);
        }

        return responseBody;
    }

    /**
     * 异步发送POST Form表单请求
     *
     * @param url      请求地址
     * @param formData form数据
     * @param headers  请求头
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> postFormAsync(String url, Map<String, String> formData, Map<String, String> headers) {
        return postFormAsync(url, formData, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送POST Form表单请求，支持自定义超时时间
     *
     * @param url      请求地址
     * @param formData form数据
     * @param headers  请求头
     * @param timeout  超时时间
     * @return CompletableFuture包装的响应内容
     */
    public static CompletableFuture<String> postFormAsync(String url, Map<String, String> formData, Map<String, String> headers, Duration timeout) {
        // 构建 form 编码字符串
        StringBuilder sb = new StringBuilder();
        if (formData != null && !formData.isEmpty()) {
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                if (!sb.isEmpty()) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        String formBody = sb.toString();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formBody));

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();

        // 打印请求信息
        if (config.isPrintJson()) {
            log.info("HttpClientUtil|postFormAsync|url={}", url);
            log.info("HttpClientUtil|postFormAsync|timeout={}", timeout);
            log.info("HttpClientUtil|postFormAsync|formData={}", formBody);
            if (headers != null && !headers.isEmpty()) {
                log.info("HttpClientUtil|postFormAsync|Headers={}", headers);
            }
        }

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    // 打印响应信息
                    if (config.isPrintJson()) {
                        log.info("HttpClientUtil|postFormAsync|Response Status={}", response.statusCode());
                        logResponseHeaders(response, "postFormAsync");
                        log.info("HttpClientUtil|postFormAsync|Response={}", response.body());
                    }
                    return response.body();
                });
    }
}