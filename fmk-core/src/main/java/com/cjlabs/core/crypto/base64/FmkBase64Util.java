package com.cjlabs.core.crypto.base64;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Lightweight Base64 helper to centralize encoding / decoding logic.
 */
public final class FmkBase64Util {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Base64.Encoder STD_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder STD_DECODER = Base64.getDecoder();
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private FmkBase64Util() {
        // utility class
    }

    public static String encode(byte[] data) {
        return data == null ? null : STD_ENCODER.encodeToString(data);
    }

    public static String encode(String text) {
        return encode(text, DEFAULT_CHARSET);
    }

    public static String encode(String text, Charset charset) {
        return text == null ? null : encode(text.getBytes(charset == null ? DEFAULT_CHARSET : charset));
    }

    public static byte[] decode(String base64) {
        return StringUtils.isBlank(base64) ? null : STD_DECODER.decode(base64);
    }

    public static String decodeToString(String base64) {
        return decodeToString(base64, DEFAULT_CHARSET);
    }

    public static String decodeToString(String base64, Charset charset) {
        byte[] bytes = decode(base64);
        return bytes == null ? null : new String(bytes, charset == null ? DEFAULT_CHARSET : charset);
    }

    public static String encodeUrlSafe(byte[] data) {
        return data == null ? null : URL_ENCODER.encodeToString(data);
    }

    public static byte[] decodeUrlSafe(String base64) {
        return StringUtils.isBlank(base64) ? null : URL_DECODER.decode(base64);
    }

    public static void main(String[] args) {
        String key = "kex";
        String value = "PVSjajhuqQSAaF4a15CUa9Qtn/1oc1r8ECkFHfAnGTk=";

        // authorization
        // Basic a2V4OlBWU2phamh1cVFTQWFGNGExNUNVYTlRdG4vMW9jMXI4RUNrRkhmQW5HVGs9

        String code = key + ":" + value;
        String encode = encode(code);
        System.out.println(encode);
    }
}