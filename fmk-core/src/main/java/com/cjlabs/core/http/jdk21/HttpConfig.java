package com.cjlabs.core.http.jdk21;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.ProxySelector;
import java.time.Duration;

@Slf4j
@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "http")
public class HttpConfig implements InitializingBean {
    // TCP 连接最长等待1秒
    // TCP 连接建立时间
    private Duration connectTimeout = Duration.ofSeconds(3);
    // 整个请求最长等待10秒
    // 从发送请求到响应结束的总时间
    private Duration requestTimeout = Duration.ofSeconds(10);
    private boolean useHttp2 = true;

    private boolean userProxy = false;
    private String proxyHost;
    private int proxyPort;
    // Windows/Mac/Linux：Java 会读取系统代理设置。
    // 如果系统没有代理，它会返回一个 ProxySelector，始终返回 Proxy.NO_PROXY，表示直接连接。
    private ProxySelector proxySelector = ProxySelector.getDefault();

    // 是否打印请求/响应 JSON 内容
    private boolean printJson = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("HttpConfig initialized: connectTimeout={}s, requestTimeout={}s, useHttp2={}, proxyHost={}, proxyPort={}",
                connectTimeout.getSeconds(), requestTimeout.getSeconds(), useHttp2, proxyHost, proxyPort);

        // 更新 HttpClientUtil 中的配置
        HttpClientUtil.updateConfig(this);
    }
}
