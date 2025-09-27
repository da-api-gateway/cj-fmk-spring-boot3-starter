package com.cjlabs.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Long 类型序列化器
 * 将 Long 类型转换为 String 类型输出，避免前端 JavaScript 大数值精度丢失问题
 * 
 * JavaScript 安全整数范围：-2^53 + 1 到 2^53 - 1
 * Java Long 范围：-2^63 到 2^63 - 1
 * 
 * @author system
 * @since 1.0.0
 */
public class LongToStringSerializer extends JsonSerializer<Long> {

    @Override
    public Class<Long> handledType() {
        return Long.class;
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.toString());
        }
    }
}
