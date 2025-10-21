package com.cjlabs.web;

import com.cjlabs.domain.enums.IEnumStr;
import com.cjlabs.web.serializer.BigDecimalSerializer;
import com.cjlabs.web.serializer.EmptyStringToNullDeserializer;
import com.cjlabs.web.serializer.EnumSerializer;
import com.cjlabs.web.serializer.LongToStringSerializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // 创建自定义模块
            SimpleModule customModule = new SimpleModule("FmkCustomSerializationModule");

            // 添加 Long 类型序列化器
            customModule.addSerializer(Long.class, new LongToStringSerializer());
            customModule.addSerializer(long.class, new LongToStringSerializer());

            // 添加 BigDecimal 序列化器
            customModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());

            // 添加枚举序列化器
            customModule.addSerializer(IEnumStr.class, new EnumSerializer());

            // 添加空字符串处理
            customModule.addDeserializer(String.class, new EmptyStringToNullDeserializer());


            // 配置构建器
            builder
                    // 添加自定义模块
                    .modules(customModule);
        };
    }
}
