package com.cjlabs.domain.common;

public interface FmkConstant {

    Long SYSTEM_USER_ID = 0L;

    String SYMBOL_UNDERLINE = "_";
    String SYMBOL_DASH = "-";
    String SYMBOL_DOT = ".";
    String SYMBOL_TILDE = "~";
    String SYMBOL_SLASH = "/";
    String SYMBOL_BACKSLASH = "\\";
    String SYMBOL_COLON = ":";
    String SYMBOL_SEMICOLON = ";";
    String SYMBOL_COMMA = ",";
    String SYMBOL_SPACE = " ";
    String SYMBOL_TAB = "\t";
    String SYMBOL_NEWLINE = "\n";
    String SYMBOL_PIPE = "|";
    String SYMBOL_STAR = "*";
    String SYMBOL_PLUS = "+";
    String SYMBOL_EQUAL = "=";
    String SYMBOL_QUESTION = "?";
    String SYMBOL_EXCLAMATION = "!";
    String SYMBOL_AT = "@";
    String SYMBOL_HASH = "#";
    String SYMBOL_DOLLAR = "$";
    String SYMBOL_PERCENT = "%";
    String SYMBOL_CARET = "^";
    String SYMBOL_AMPERSAND = "&";
    String SYMBOL_LEFT_PAREN = "(";
    String SYMBOL_RIGHT_PAREN = ")";
    String SYMBOL_LEFT_BRACE = "{";
    String SYMBOL_RIGHT_BRACE = "}";
    String SYMBOL_LEFT_BRACKET = "[";
    String SYMBOL_RIGHT_BRACKET = "]";
    String SYMBOL_LESS_THAN = "<";
    String SYMBOL_GREATER_THAN = ">";
    String SYMBOL_QUOTE = "\"";
    String SYMBOL_SINGLE_QUOTE = "'";
    String SYMBOL_BACKTICK = "`";


    /**
     * traceId
     */
    String HEADER_TRACE_ID = "X-CJ-Trace-Id";
    /**
     * 用户的设备信息
     */
    String HEADER_DEVICE_ID = "X-CJ-Device-Id";
    /**
     * 用户token，可以拿到用户的一些信息
     */
    String HEADER_USER_TOKEN = "X-CJ-Token";
    /**
     * 用户的设备版本
     */
    String HEADER_DEVICE_VERSION = "X-CJ-Device-Version";
    // /**
    //  * {@link DeviceTypeEnum}
    //  */
    // String HEADER_DEVICE_TYPE = "X-CJ-Device_Type";

    String HEADER_DEVICE_LANGUAGE = "X-CJ-Accept-Language";

    String HEADER_USER_AGENT = "User-Agent";

    String HEADER_REFERER = "Referer";

    /**
     * traceId
     */
    String MDC_TRACE_ID = "X-Trace-Id";
    String MDC_SPAN_ID = "X-Span-Id";
    String MDC_USER_ID = "X-User-Id";

}
