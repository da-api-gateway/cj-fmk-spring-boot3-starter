package com.cjlabs.web.util.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.http.HttpClient;

// HTTP 版本枚举
@Getter
@AllArgsConstructor
public enum FmkHttpVersion {

    HTTP_1_1(HttpClient.Version.HTTP_1_1),
    HTTP_2(HttpClient.Version.HTTP_2);

    private final HttpClient.Version clientVersion;

}