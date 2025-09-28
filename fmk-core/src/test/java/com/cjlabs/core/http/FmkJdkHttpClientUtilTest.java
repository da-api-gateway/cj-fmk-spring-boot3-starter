package com.cjlabs.core.http;

import com.cjlabs.core.http.jdk21.FmkJdkHttpClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FmkJdkHttpClientUtil Tests")
class FmkJdkHttpClientUtilTest {

    private static final String TEST_URL = "https://api.example.com/test";
    private HttpClient mockHttpClient;
    private HttpResponse<String> mockResponse;

    @BeforeEach
    void setUp() throws Exception {
        // 创建模拟对象
        mockHttpClient = mock(HttpClient.class);
        mockResponse = mock(HttpResponse.class);
        
        // 设置模拟响应
        when(mockResponse.body()).thenReturn("{\"status\":\"success\"}");
        when(mockResponse.statusCode()).thenReturn(200);
        
        // 使用反射设置静态字段
        setMockHttpClient(mockHttpClient);
        
        // 配置HTTP客户端
        FmkHttpConfig config = new FmkHttpConfig();
        config.setPrintJson(false); // 测试时不打印日志
        config.setConnectTimeout(Duration.ofSeconds(5));
        config.setRequestTimeout(Duration.ofSeconds(5));
        config.getJdk().setHttpVersion(FmkHttpVersion.HTTP_2);
        FmkJdkHttpClientUtil.updateConfig(config);
    }

    // 使用反射设置静态字段
    private void setMockHttpClient(HttpClient client) throws Exception {
        java.lang.reflect.Field clientField = FmkJdkHttpClientUtil.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(null, client);
    }

    @Nested
    @DisplayName("GET Request Tests")
    class GetRequestTests {
        
        @Test
        @DisplayName("get should send simple GET request")
        void getShouldSendSimpleGetRequest() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 执行GET请求
            String response = FmkJdkHttpClientUtil.get(TEST_URL);
            
            // 验证响应
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals(URI.create(TEST_URL), capturedRequest.uri());
            assertEquals("GET", capturedRequest.method());
        }
        
        @Test
        @DisplayName("get should send GET request with headers")
        void getShouldSendGetRequestWithHeaders() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 准备请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer token123");
            headers.put("Custom-Header", "CustomValue");
            
            // 执行GET请求
            FmkJdkHttpClientUtil.get(TEST_URL, headers);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("Bearer token123", capturedRequest.headers().firstValue("Authorization").orElse(null));
            assertEquals("CustomValue", capturedRequest.headers().firstValue("Custom-Header").orElse(null));
        }
        
        @Test
        @DisplayName("get should send GET request with custom timeout")
        void getShouldSendGetRequestWithCustomTimeout() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 执行GET请求，使用自定义超时
            Duration timeout = Duration.ofSeconds(10);
            FmkJdkHttpClientUtil.get(TEST_URL, null, timeout);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals(timeout, capturedRequest.timeout().orElse(null));
        }
        
        @Test
        @DisplayName("getAsync should send asynchronous GET request")
        void getAsyncShouldSendAsynchronousGetRequest() throws ExecutionException, InterruptedException {
            // 设置模拟异步响应
            CompletableFuture<HttpResponse<String>> futureResponse = CompletableFuture.completedFuture(mockResponse);
            when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(futureResponse);
            
            // 执行异步GET请求
            CompletableFuture<String> future = FmkJdkHttpClientUtil.getAsync(TEST_URL, null);
            
            // 等待并验证响应
            String response = future.get();
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals(URI.create(TEST_URL), capturedRequest.uri());
            assertEquals("GET", capturedRequest.method());
        }
    }
    
    @Nested
    @DisplayName("POST JSON Tests")
    class PostJsonTests {
        
        @Test
        @DisplayName("postJson should send JSON POST request")
        void postJsonShouldSendJsonPostRequest() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 准备请求JSON
            String requestJson = "{\"name\":\"John\",\"age\":30}";
            
            // 执行POST JSON请求
            String response = FmkJdkHttpClientUtil.postJson(TEST_URL, requestJson);
            
            // 验证响应
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("POST", capturedRequest.method());
            assertEquals("application/json", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
        
        @Test
        @DisplayName("postJson should send JSON POST request with headers")
        void postJsonShouldSendJsonPostRequestWithHeaders() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 准备请求头和JSON
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer token123");
            String requestJson = "{\"key\":\"value\"}";
            
            // 执行POST JSON请求
            FmkJdkHttpClientUtil.postJson(TEST_URL, requestJson, headers);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("Bearer token123", capturedRequest.headers().firstValue("Authorization").orElse(null));
            assertEquals("application/json", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
        
        @Test
        @DisplayName("postJsonAsync should send asynchronous JSON POST request")
        void postJsonAsyncShouldSendAsynchronousJsonPostRequest() throws ExecutionException, InterruptedException {
            // 设置模拟异步响应
            CompletableFuture<HttpResponse<String>> futureResponse = CompletableFuture.completedFuture(mockResponse);
            when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(futureResponse);
            
            // 准备请求JSON
            String requestJson = "{\"async\":true}";
            
            // 执行异步POST JSON请求
            CompletableFuture<String> future = FmkJdkHttpClientUtil.postJsonAsync(TEST_URL, requestJson, null);
            
            // 等待并验证响应
            String response = future.get();
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("POST", capturedRequest.method());
            assertEquals("application/json", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
    }
    
    @Nested
    @DisplayName("POST Form Tests")
    class PostFormTests {
        
        @Test
        @DisplayName("postForm should send form POST request")
        void postFormShouldSendFormPostRequest() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 准备表单数据
            Map<String, String> formData = new HashMap<>();
            formData.put("username", "john");
            formData.put("password", "secret");
            
            // 执行POST表单请求
            String response = FmkJdkHttpClientUtil.postForm(TEST_URL, formData);
            
            // 验证响应
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("POST", capturedRequest.method());
            assertEquals("application/x-www-form-urlencoded", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
        
        @Test
        @DisplayName("postForm should send form POST request with headers")
        void postFormShouldSendFormPostRequestWithHeaders() throws IOException, InterruptedException {
            // 设置模拟响应
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
            
            // 准备请求头和表单数据
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Custom-Header", "CustomValue");
            
            Map<String, String> formData = new HashMap<>();
            formData.put("field1", "value1");
            formData.put("field2", "value2");
            
            // 执行POST表单请求
            FmkJdkHttpClientUtil.postForm(TEST_URL, formData, headers);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("CustomValue", capturedRequest.headers().firstValue("X-Custom-Header").orElse(null));
            assertEquals("application/x-www-form-urlencoded", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
        
        @Test
        @DisplayName("postFormAsync should send asynchronous form POST request")
        void postFormAsyncShouldSendAsynchronousFormPostRequest() throws ExecutionException, InterruptedException {
            // 设置模拟异步响应
            CompletableFuture<HttpResponse<String>> futureResponse = CompletableFuture.completedFuture(mockResponse);
            when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(futureResponse);
            
            // 准备表单数据
            Map<String, String> formData = new HashMap<>();
            formData.put("async", "true");
            formData.put("data", "test");
            
            // 执行异步POST表单请求
            CompletableFuture<String> future = FmkJdkHttpClientUtil.postFormAsync(TEST_URL, formData, null);
            
            // 等待并验证响应
            String response = future.get();
            assertEquals("{\"status\":\"success\"}", response);
            
            // 验证请求
            ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(mockHttpClient).sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
            
            HttpRequest capturedRequest = requestCaptor.getValue();
            assertEquals("POST", capturedRequest.method());
            assertEquals("application/x-www-form-urlencoded", capturedRequest.headers().firstValue("Content-Type").orElse(null));
        }
    }
    
    @Nested
    @DisplayName("Config Tests")
    class ConfigTests {
        
        @Test
        @DisplayName("updateConfig should update HTTP client configuration")
        void updateConfigShouldUpdateHttpClientConfiguration() {
            // 使用静态模拟来验证 HttpClient.newBuilder() 的调用
            try (MockedStatic<HttpClient> mockedHttpClient = Mockito.mockStatic(HttpClient.class)) {
                // 设置模拟构建器
                HttpClient.Builder mockBuilder = mock(HttpClient.Builder.class);
                when(mockBuilder.connectTimeout(any())).thenReturn(mockBuilder);
                when(mockBuilder.proxy(any())).thenReturn(mockBuilder);
                when(mockBuilder.version(any())).thenReturn(mockBuilder);
                when(mockBuilder.build()).thenReturn(mockHttpClient);
                
                mockedHttpClient.when(HttpClient::newBuilder).thenReturn(mockBuilder);
                
                // 更新配置
                FmkHttpConfig newConfig = new FmkHttpConfig();
                newConfig.setPrintJson(false);
                newConfig.setConnectTimeout(Duration.ofSeconds(10));
                newConfig.setRequestTimeout(Duration.ofSeconds(15));
                newConfig.getJdk().setHttpVersion(FmkHttpVersion.HTTP_1_1);
                
                FmkJdkHttpClientUtil.updateConfig(newConfig);
                
                // 验证 HttpClient.newBuilder() 被调用
                mockedHttpClient.verify(HttpClient::newBuilder);
                
                // 验证配置被应用
                verify(mockBuilder).connectTimeout(Duration.ofSeconds(10));
                verify(mockBuilder).version(any());
                verify(mockBuilder).build();
            }
        }
        
        @Test
        @DisplayName("updateConfig should configure proxy if specified")
        void updateConfigShouldConfigureProxyIfSpecified() {
            // 使用静态模拟来验证 HttpClient.newBuilder() 的调用
            try (MockedStatic<HttpClient> mockedHttpClient = Mockito.mockStatic(HttpClient.class)) {
                // 设置模拟构建器
                HttpClient.Builder mockBuilder = mock(HttpClient.Builder.class);
                when(mockBuilder.connectTimeout(any())).thenReturn(mockBuilder);
                when(mockBuilder.proxy(any())).thenReturn(mockBuilder);
                when(mockBuilder.version(any())).thenReturn(mockBuilder);
                when(mockBuilder.build()).thenReturn(mockHttpClient);
                
                mockedHttpClient.when(HttpClient::newBuilder).thenReturn(mockBuilder);
                
                // 创建带有代理配置的配置
                FmkHttpConfig proxyConfig = new FmkHttpConfig();
                proxyConfig.setUseProxy(true);
                proxyConfig.setProxyHost("localhost");
                proxyConfig.setProxyPort(8888);
                
                // 更新配置
                FmkJdkHttpClientUtil.updateConfig(proxyConfig);
                
                // 验证代理配置被应用
                verify(mockBuilder).proxy(any());
            }
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("get should handle HTTP error responses")
        void getShouldHandleHttpErrorResponses() throws IOException, InterruptedException {
            // 设置错误响应
            HttpResponse<String> errorResponse = mock(HttpResponse.class);
            when(errorResponse.body()).thenReturn("Not Found");
            when(errorResponse.statusCode()).thenReturn(404);
            
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(errorResponse);
            
            // 执行GET请求
            String response = FmkJdkHttpClientUtil.get(TEST_URL);
            
            // 验证响应
            assertEquals("Not Found", response);
        }
        
        @Test
        @DisplayName("get should handle IOException")
        void getShouldHandleIOException() throws IOException, InterruptedException {
            // 设置异常
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(new IOException("Connection failed"));
            
            // 验证异常被抛出
            assertThrows(IOException.class, () -> FmkJdkHttpClientUtil.get(TEST_URL));
        }
    }
}