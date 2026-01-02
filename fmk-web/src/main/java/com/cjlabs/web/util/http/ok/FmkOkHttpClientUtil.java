package com.cjlabs.web.util.http.ok;

import com.cjlabs.web.threadlocal.FmkContextUtil;
import com.cjlabs.web.util.http.FmkHttpConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.cjlabs.domain.common.FmkConstant.HEADER_SPAN_ID;
import static com.cjlabs.domain.common.FmkConstant.HEADER_TRACE_ID;

/**
 * OkHttp3 HTTP 客户端工具类
 * 支持 GET、POST JSON、POST Form 等常见请求方式
 * 集成链路追踪 (TraceId/SpanId) 支持
 */
@Slf4j
public final class FmkOkHttpClientUtil {

    /**
     * -- GETTER --
     *  获取当前的 OkHttpClient 实例
     */
    @Getter
    private static volatile OkHttpClient client;
    private static volatile FmkHttpConfig config;

    static {
        // 初始化默认配置
        config = new FmkHttpConfig();
        rebuildClient();
    }

    /**
     * 修改配置并重建 OkHttpClient
     */
    public static synchronized void updateConfig(FmkHttpConfig newConfig) {
        config = newConfig;
        rebuildClient();
        log.info("OkHttpClient rebuilt with new configuration");
    }

    private static void rebuildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true);

        // 配置代理
        if (config.isUseProxy() && StringUtils.isNotBlank(config.getProxyHost()) && config.getProxyPort() > 0) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort()));
            builder.proxy(proxy);
            log.info("OkHttpClient configured with proxy: {}:{}", config.getProxyHost(), config.getProxyPort());
        }

        // 添加日志拦截器（仅在打印 JSON 时启用）
        if (config.isPrintJson()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::info);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // 添加 Trace 拦截器（自动注入 TraceId 和 SpanId）
        builder.addInterceptor(new TraceInterceptor());

        client = builder.build();
    }

    /**
     * Trace 拦截器 - 自动在请求头中注入 TraceId 和 SpanId
     */
    private static class TraceInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();

            // 从上下文中获取 TraceId 和 SpanId
            try {
                String traceId = FmkContextUtil.getTraceIdString(null);
                String spanId = FmkContextUtil.getSpanIdString(null);

                if (StringUtils.isNotBlank(traceId)) {
                    requestBuilder.header(HEADER_TRACE_ID, traceId);
                }
                if (StringUtils.isNotBlank(spanId)) {
                    requestBuilder.header(HEADER_SPAN_ID, spanId);
                }
            } catch (Exception e) {
                log.warn("FmkOkHttpClientUtil|TraceInterceptor|获取trace信息失败|error={}", e.getMessage());
            }

            Request newRequest = requestBuilder.build();
            Response response = chain.proceed(newRequest);

            if (config.isPrintJson()) {
                logResponseHeaders(response);
            }

            return response;
        }
    }

    /**
     * 打印响应头信息
     */
    private static void logResponseHeaders(Response response) {
        Headers headers = response.headers();
        if (headers.size() > 0) {
            log.info("FmkOkHttpClientUtil|Response Headers:");
            for (int i = 0; i < headers.size(); i++) {
                log.info("FmkOkHttpClientUtil|{} = {}", headers.name(i), headers.value(i));
            }
        }
    }

    /**
     * 发送 GET 请求
     */
    public static String get(String url) throws IOException {
        return get(url, null, config.getRequestTimeout());
    }

    /**
     * 发送 GET 请求，支持自定义 headers
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, config.getRequestTimeout());
    }

    /**
     * 发送 GET 请求，支持自定义超时时间
     */
    public static String get(String url, Map<String, String> headers, Duration timeout) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|get|url={}", url);
            log.info("FmkOkHttpClientUtil|get|timeout={}", timeout);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|get|Headers: {}", headers);
            }
        }

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String body = responseBody != null ? responseBody.string() : "";

            if (config.isPrintJson()) {
                log.info("FmkOkHttpClientUtil|get|Response Status={}", response.code());
                log.info("FmkOkHttpClientUtil|get|Response={}", body);
            }

            return body;
        }
    }

    /**
     * 异步发送 GET 请求
     */
    public static CompletableFuture<String> getAsync(String url) {
        return getAsync(url, null, config.getRequestTimeout());
    }

    /**
     * 异步发送 GET 请求，支持自定义 headers
     */
    public static CompletableFuture<String> getAsync(String url, Map<String, String> headers) {
        return getAsync(url, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送 GET 请求，支持自定义超时时间
     */
    public static CompletableFuture<String> getAsync(String url, Map<String, String> headers, Duration timeout) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|getAsync|url={}", url);
            log.info("FmkOkHttpClientUtil|getAsync|timeout={}", timeout);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|getAsync|Headers: {}", headers);
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("FmkOkHttpClientUtil|getAsync|failed|error={}", e.getMessage(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    String body = responseBody != null ? responseBody.string() : "";

                    if (config.isPrintJson()) {
                        log.info("FmkOkHttpClientUtil|getAsync|Response Status={}", response.code());
                        log.info("FmkOkHttpClientUtil|getAsync|Response={}", body);
                    }

                    future.complete(body);
                } catch (Exception e) {
                    log.error("FmkOkHttpClientUtil|getAsync|error={}", e.getMessage(), e);
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * 发送 POST JSON 请求
     */
    public static String postJson(String url, String json) throws IOException {
        return postJson(url, json, null, config.getRequestTimeout());
    }

    /**
     * 发送 POST JSON 请求，支持自定义 headers
     */
    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        return postJson(url, json, headers, config.getRequestTimeout());
    }

    /**
     * 发送 POST JSON 请求，支持自定义超时时间
     */
    public static String postJson(String url, String json, Map<String, String> headers, Duration timeout) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|postJson|url={}", url);
            log.info("FmkOkHttpClientUtil|postJson|timeout={}", timeout);
            log.info("FmkOkHttpClientUtil|postJson|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|postJson|Headers: {}", headers);
            }
        }

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseStr = responseBody != null ? responseBody.string() : "";

            if (config.isPrintJson()) {
                log.info("FmkOkHttpClientUtil|postJson|Response Status={}", response.code());
                log.info("FmkOkHttpClientUtil|postJson|Response={}", responseStr);
            }

            return responseStr;
        }
    }

    /**
     * 异步发送 POST JSON 请求
     */
    public static CompletableFuture<String> postJsonAsync(String url, String json) {
        return postJsonAsync(url, json, null, config.getRequestTimeout());
    }

    /**
     * 异步发送 POST JSON 请求，支持自定义 headers
     */
    public static CompletableFuture<String> postJsonAsync(String url, String json, Map<String, String> headers) {
        return postJsonAsync(url, json, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送 POST JSON 请求，支持自定义超时时间
     */
    public static CompletableFuture<String> postJsonAsync(String url, String json, Map<String, String> headers, Duration timeout) {
        CompletableFuture<String> future = new CompletableFuture<>();

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|postJsonAsync|url={}", url);
            log.info("FmkOkHttpClientUtil|postJsonAsync|timeout={}", timeout);
            log.info("FmkOkHttpClientUtil|postJsonAsync|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|postJsonAsync|Headers: {}", headers);
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("FmkOkHttpClientUtil|postJsonAsync|failed|error={}", e.getMessage(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    String responseStr = responseBody != null ? responseBody.string() : "";

                    if (config.isPrintJson()) {
                        log.info("FmkOkHttpClientUtil|postJsonAsync|Response Status={}", response.code());
                        log.info("FmkOkHttpClientUtil|postJsonAsync|Response={}", responseStr);
                    }

                    future.complete(responseStr);
                } catch (Exception e) {
                    log.error("FmkOkHttpClientUtil|postJsonAsync|error={}", e.getMessage(), e);
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * 发送 POST Form 表单请求
     */
    public static String postForm(String url, Map<String, String> formData) throws IOException {
        return postForm(url, formData, null, config.getRequestTimeout());
    }

    /**
     * 发送 POST Form 表单请求，支持自定义 headers
     */
    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers) throws IOException {
        return postForm(url, formData, headers, config.getRequestTimeout());
    }

    /**
     * 发送 POST Form 表单请求，支持自定义超时时间
     */
    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers, Duration timeout) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (MapUtils.isNotEmpty(formData)) {
            formData.forEach(formBuilder::add);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|postForm|url={}", url);
            log.info("FmkOkHttpClientUtil|postForm|timeout={}", timeout);
            log.info("FmkOkHttpClientUtil|postForm|formData={}", formData);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|postForm|Headers={}", headers);
            }
        }

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseStr = responseBody != null ? responseBody.string() : "";

            if (config.isPrintJson()) {
                log.info("FmkOkHttpClientUtil|postForm|Response Status={}", response.code());
                log.info("FmkOkHttpClientUtil|postForm|Response={}", responseStr);
            }

            return responseStr;
        }
    }

    /**
     * 异步发送 POST Form 表单请求
     */
    public static CompletableFuture<String> postFormAsync(String url, Map<String, String> formData) {
        return postFormAsync(url, formData, null, config.getRequestTimeout());
    }

    /**
     * 异步发送 POST Form 表单请求，支持自定义 headers
     */
    public static CompletableFuture<String> postFormAsync(String url, Map<String, String> formData, Map<String, String> headers) {
        return postFormAsync(url, formData, headers, config.getRequestTimeout());
    }

    /**
     * 异步发送 POST Form 表单请求，支持自定义超时时间
     */
    public static CompletableFuture<String> postFormAsync(String url, Map<String, String> formData, Map<String, String> headers, Duration timeout) {
        CompletableFuture<String> future = new CompletableFuture<>();

        FormBody.Builder formBuilder = new FormBody.Builder();

        if (MapUtils.isNotEmpty(formData)) {
            formData.forEach(formBuilder::add);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|postFormAsync|url={}", url);
            log.info("FmkOkHttpClientUtil|postFormAsync|timeout={}", timeout);
            log.info("FmkOkHttpClientUtil|postFormAsync|formData={}", formData);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|postFormAsync|Headers={}", headers);
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("FmkOkHttpClientUtil|postFormAsync|failed|error={}", e.getMessage(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    String responseStr = responseBody != null ? responseBody.string() : "";

                    if (config.isPrintJson()) {
                        log.info("FmkOkHttpClientUtil|postFormAsync|Response Status={}", response.code());
                        log.info("FmkOkHttpClientUtil|postFormAsync|Response={}", responseStr);
                    }

                    future.complete(responseStr);
                } catch (Exception e) {
                    log.error("FmkOkHttpClientUtil|postFormAsync|error={}", e.getMessage(), e);
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * 发送 PUT 请求
     */
    public static String put(String url, String json) throws IOException {
        return put(url, json, null, config.getRequestTimeout());
    }

    /**
     * 发送 PUT 请求，支持自定义 headers 和超时时间
     */
    public static String put(String url, String json, Map<String, String> headers, Duration timeout) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .put(body);

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|put|url={}", url);
            log.info("FmkOkHttpClientUtil|put|json={}", json);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|put|Headers: {}", headers);
            }
        }

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseStr = responseBody != null ? responseBody.string() : "";

            if (config.isPrintJson()) {
                log.info("FmkOkHttpClientUtil|put|Response Status={}", response.code());
                log.info("FmkOkHttpClientUtil|put|Response={}", responseStr);
            }

            return responseStr;
        }
    }

    /**
     * 发送 DELETE 请求
     */
    public static String delete(String url) throws IOException {
        return delete(url, null, config.getRequestTimeout());
    }

    /**
     * 发送 DELETE 请求，支持自定义 headers 和超时时间
     */
    public static String delete(String url, Map<String, String> headers, Duration timeout) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .delete();

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }

        Request request = requestBuilder.build();

        if (config.isPrintJson()) {
            log.info("FmkOkHttpClientUtil|delete|url={}", url);
            if (MapUtils.isNotEmpty(headers)) {
                log.info("FmkOkHttpClientUtil|delete|Headers: {}", headers);
            }
        }

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseStr = responseBody != null ? responseBody.string() : "";

            if (config.isPrintJson()) {
                log.info("FmkOkHttpClientUtil|delete|Response Status={}", response.code());
                log.info("FmkOkHttpClientUtil|delete|Response={}", responseStr);
            }

            return responseStr;
        }
    }

    /**
     * 私有构造函数，防止实例化
     */
    private FmkOkHttpClientUtil() {
        throw new AssertionError("No instances");
    }
}