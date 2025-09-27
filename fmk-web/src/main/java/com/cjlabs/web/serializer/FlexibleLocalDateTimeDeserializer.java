package com.cjlabs.web.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xodo.fmk.time.FmkTimeConstant;

import java.io.IOException;
import java.time.LocalDateTime;

public class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String str = p.getText();

        try {
            return LocalDateTime.parse(str, FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter);
        } catch (Exception e) {
            return LocalDateTime.parse(str, FmkTimeConstant.yy_MM_dd_T_HH_mm_ss_Formatter);
        }
    }
}
