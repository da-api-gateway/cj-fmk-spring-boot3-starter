package com.cjlabs.core.http.jdk21;

import com.cjlabs.core.http.FmkHttpConfig;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class FmkJdkWebSocketClientUtil {

    private static final Logger log = LoggerFactory.getLogger(FmkJdkWebSocketClientUtil.class);

    private static volatile HttpClient client;
    private static volatile FmkHttpConfig config;

    static {
        config = new FmkHttpConfig();
        rebuildClient();
    }

    private FmkJdkWebSocketClientUtil() {
    }

    public static synchronized void updateConfig(FmkHttpConfig newConfig) {
        config = Objects.requireNonNull(newConfig, "newConfig must not be null");

        if (newConfig.isUseProxy()) {
            if (StringUtils.isNotBlank(newConfig.getProxyHost()) && newConfig.getProxyPort() > 0) {
                ProxySelector proxySelector = ProxySelector.of(
                        new InetSocketAddress(newConfig.getProxyHost(), newConfig.getProxyPort())
                );
                newConfig.setProxySelector(proxySelector);
                log.info("WebSocketClient configured with proxy: {}:{}", newConfig.getProxyHost(), newConfig.getProxyPort());
            } else {
                newConfig.setProxySelector(ProxySelector.getDefault());
            }
        }

        rebuildClient();
        log.info("WebSocket client rebuilt with new configuration");
    }

    private static void rebuildClient() {
        client = HttpClient.newBuilder()
                .connectTimeout(config.getConnectTimeout())
                .proxy(config.getProxySelector())
                .version(config.getJdk().getHttpVersion().getClientVersion())
                .build();
    }

    public static CompletableFuture<WebSocket> connect(String url, WebSocket.Listener listener) {
        return connect(url, listener, null, null, config.getConnectTimeout());
    }

    public static CompletableFuture<WebSocket> connect(String url,
                                                       WebSocket.Listener listener,
                                                       Map<String, String> headers) {
        return connect(url, listener, headers, null, config.getConnectTimeout());
    }

    public static CompletableFuture<WebSocket> connect(String url,
                                                       WebSocket.Listener listener,
                                                       Map<String, String> headers,
                                                       List<String> subprotocols) {
        return connect(url, listener, headers, subprotocols, config.getConnectTimeout());
    }

    public static CompletableFuture<WebSocket> connect(String url,
                                                       WebSocket.Listener listener,
                                                       Map<String, String> headers,
                                                       List<String> subprotocols,
                                                       Duration handshakeTimeout) {
        Objects.requireNonNull(url, "url must not be null");
        Objects.requireNonNull(listener, "listener must not be null");
        Objects.requireNonNull(handshakeTimeout, "handshakeTimeout must not be null");

        WebSocket.Builder builder = client.newWebSocketBuilder()
                .connectTimeout(handshakeTimeout);

        if (headers != null) {
            headers.forEach(builder::header);
        }

        if (subprotocols != null && !subprotocols.isEmpty()) {
            String mostPreferred = subprotocols.get(0);
            String[] lesserPreferred = subprotocols.size() > 1
                    ? subprotocols.subList(1, subprotocols.size()).toArray(new String[0])
                    : new String[0];
            builder.subprotocols(mostPreferred, lesserPreferred);
        }

        if (config.isPrintJson()) {
            log.info("WebSocketClient|connect|url={}", url);
            log.info("WebSocketClient|connect|timeout={}", handshakeTimeout);
            if (headers != null && !headers.isEmpty()) {
                log.info("WebSocketClient|connect|Headers={}", headers);
            }
            if (subprotocols != null && !subprotocols.isEmpty()) {
                log.info("WebSocketClient|connect|Subprotocols={}", subprotocols);
            }
        }

        URI uri = URI.create(url);
        return builder.buildAsync(uri, listener)
                .whenComplete((webSocket, throwable) -> {
                    if (config.isPrintJson()) {
                        if (throwable == null) {
                            log.info("WebSocketClient|connect|Success|subprotocol={}", webSocket.getSubprotocol());
                        } else {
                            log.error("WebSocketClient|connect|Failed", throwable);
                        }
                    }
                });
    }

    public static CompletableFuture<WebSocket> sendText(WebSocket webSocket, String message) {
        return sendText(webSocket, message, true);
    }

    public static CompletableFuture<WebSocket> sendText(WebSocket webSocket, String message, boolean last) {
        Objects.requireNonNull(webSocket, "webSocket must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (config.isPrintJson()) {
            log.info("WebSocketClient|sendText|last={}|payload={}", last, message);
        }

        return webSocket.sendText(message, last);
    }

    public static CompletableFuture<WebSocket> sendBinary(WebSocket webSocket, ByteBuffer payload, boolean last) {
        Objects.requireNonNull(webSocket, "webSocket must not be null");
        Objects.requireNonNull(payload, "payload must not be null");

        if (config.isPrintJson()) {
            log.info("WebSocketClient|sendBinary|last={}|size={}", last, payload.remaining());
        }

        return webSocket.sendBinary(payload, last);
    }

    public static CompletableFuture<WebSocket> sendPing(WebSocket webSocket, ByteBuffer message) {
        Objects.requireNonNull(webSocket, "webSocket must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (config.isPrintJson()) {
            log.info("WebSocketClient|sendPing|size={}", message.remaining());
        }

        return webSocket.sendPing(message);
    }

    public static CompletableFuture<WebSocket> sendPong(WebSocket webSocket, ByteBuffer message) {
        Objects.requireNonNull(webSocket, "webSocket must not be null");
        Objects.requireNonNull(message, "message must not be null");

        if (config.isPrintJson()) {
            log.info("WebSocketClient|sendPong|size={}", message.remaining());
        }

        return webSocket.sendPong(message);
    }

    public static CompletableFuture<WebSocket> close(WebSocket webSocket, int statusCode, String reason) {
        Objects.requireNonNull(webSocket, "webSocket must not be null");

        if (config.isPrintJson()) {
            log.info("WebSocketClient|close|status={}|reason={}", statusCode, reason);
        }

        return webSocket.sendClose(statusCode, reason);
    }
}