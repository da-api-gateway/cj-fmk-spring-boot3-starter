package com.cjlabs.boot.autoconfig;

import com.cjlabs.boot.controller.FmkBootController;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
// @ComponentScan(basePackages = "com.cjlabs.boot")
public class FmkBootAutoConfiguration {
    // 这里会自动扫描并注册 com.cjlabs.boot 包下的所有组件，包括 Controller

    @Bean
    public FmkBootController fmkBootController() {
        return new FmkBootController();
    }


}