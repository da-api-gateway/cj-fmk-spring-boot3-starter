package com.cjlabs.web.serializer;

import com.cjlabs.domain.enums.IEnumStr;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 枚举序列化器
 * 将实现了IEnumStr接口的枚举转换为前端友好的格式
 */
public class EnumSerializer extends JsonSerializer<IEnumStr> {

    @Override
    public void serialize(IEnumStr value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // 输出包含code和desc的对象
        gen.writeStartObject();
        gen.writeStringField("code", value.getCode());
        gen.writeStringField("msg", value.getMsg());
        gen.writeEndObject();
    }
}