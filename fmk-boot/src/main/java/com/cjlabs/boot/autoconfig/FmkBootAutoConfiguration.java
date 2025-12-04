package com.cjlabs.boot.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@AutoConfiguration
@ComponentScan(basePackages = "com.cjlabs.boot")
@MapperScan(
        basePackages = "com.cjlabs.boot",
        markerInterface = com.baomidou.mybatisplus.core.mapper.BaseMapper.class
)
public class FmkBootAutoConfiguration {
    // 这里会自动扫描并注册 com.cjlabs.boot 包下的所有组件，包括 Controller

    public FmkBootAutoConfiguration() {
        log.info("FmkBootAutoConfiguration|初始化|自动配置已加载");
    }


}