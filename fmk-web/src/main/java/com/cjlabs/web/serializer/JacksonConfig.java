package com.cjlabs.web.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xodo.fmk.time.FmkTimeConstant;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

            // 配置构建器
            builder
                    // 时间序列化配置
                    .serializers(new LocalDateTimeSerializer(FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter))
                    .deserializers(new FlexibleLocalDateTimeDeserializer())
                    // 添加自定义模块
                    .modules(customModule);
        };
    }
}
