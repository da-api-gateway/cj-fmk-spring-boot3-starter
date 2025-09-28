package com.cjlabs.core.http;

import lombok.Getter;
import lombok.Setter;

import java.net.ProxySelector;
import java.time.Duration;

@Getter
@Setter
public class FmkHttpConfig {
    // TCP 连接最长等待1秒
    // TCP 连接建立时间
    private Duration connectTimeout = Duration.ofSeconds(3);
    // 整个请求最长等待10秒
    // 从发送请求到响应结束的总时间
    private Duration requestTimeout = Duration.ofSeconds(10);

    private boolean useProxy = false;
    private String proxyHost;
    private int proxyPort;
    // Windows/Mac/Linux：Java 会读取系统代理设置。
    // 如果系统没有代理，它会返回一个 ProxySelector，始终返回 Proxy.NO_PROXY，表示直接连接。
    private ProxySelector proxySelector = ProxySelector.getDefault();

    // 是否打印请求/响应 JSON 内容
    private boolean printJson = false;

    // JDK HttpClient 特有配置
    private Jdk jdk = new Jdk();

    // Apache HttpClient 特有配置
    private Apache apache = new Apache();

    @Getter
    @Setter
    public static class Jdk {
        // 替换布尔值为枚举类型
        private FmkHttpVersion httpVersion = FmkHttpVersion.HTTP_2;
        // 是否启用 JDK HttpClient
        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class Apache {
        private int maxConnections = 100;
        private int maxConnectionsPerRoute = 20;
        // 是否启用 Apache HttpClient
        private boolean enabled = true;
    }
}
