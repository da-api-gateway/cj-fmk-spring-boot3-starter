package com.cjlabs.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal序列化器
 * 统一金额格式，避免科学计数法，保留2位小数
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            // 保留18位小数，四舍五入
            gen.writeString(value.setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        }
    }
}