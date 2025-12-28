package com.cjlabs.domain.common;

/**
 * 框架常量定义
 * 包含各类符号、HTTP请求头和日志MDC键名
 */
public interface FmkConstant {

    /**
     * 系统相关常量
     */
    Long SYSTEM_USER_ID = 0L;

    /**
     * HTTP 请求头常量
     */
    String HEADER_TRACE_ID = "X-CJ-Trace-Id";       // 链路追踪ID
    String HEADER_SPAN_ID = "X-CJ-Span-Id";       // 链路追踪ID
    String HEADER_USER_TOKEN = "X-CJ-Token";        // 用户令牌
    String HEADER_DEVICE_ID = "X-CJ-Device-Id";     // 设备ID
    String HEADER_DEVICE_VERSION = "X-CJ-Device-Version"; // 设备版本
    String HEADER_CLIENT_TYPE = "X-CJ-CLIENT_TYPE"; // 设备类型
    String HEADER_DEVICE_LANGUAGE = "X-CJ-Accept-Language"; // 设备语言


    String HEADER_USER_AGENT = "User-Agent";        // 用户代理
    String HEADER_REFERER = "Referer";              // 引用页

    /**
     * 日志MDC上下文键名
     */
    String MDC_TRACE_ID = "X-CJ-Trace-Id";          // 链路追踪ID
    String MDC_SPAN_ID = "X-CJ-Span-Id";            // 跨度ID
    String MDC_USER_ID = "X-CJ-User-Id";            // 用户ID
    String MDC_JOB_ID = "X-CJ-Job-Id";            // JobID

    /**
     * 常用符号 - 基础
     */
    String SYMBOL_SPACE = " ";                      // 空格
    String SYMBOL_TAB = "\t";                       // 制表符
    String SYMBOL_NEWLINE = "\n";                   // 换行符

    /**
     * 常用符号 - 标点
     */
    String SYMBOL_DOT = ".";                        // 点
    String SYMBOL_COMMA = ",";                      // 逗号
    String SYMBOL_COLON = ":";                      // 冒号
    String SYMBOL_SEMICOLON = ";";                  // 分号
    String SYMBOL_QUESTION = "?";                   // 问号
    String SYMBOL_EXCLAMATION = "!";                // 感叹号
    String SYMBOL_QUOTE = "\"";                     // 双引号
    String SYMBOL_SINGLE_QUOTE = "'";               // 单引号
    String SYMBOL_BACKTICK = "`";                   // 反引号

    /**
     * 常用符号 - 运算符
     */
    String SYMBOL_PLUS = "+";                       // 加号
    String SYMBOL_DASH = "-";                       // 减号/破折号
    String SYMBOL_STAR = "*";                       // 星号
    String SYMBOL_SLASH = "/";                      // 正斜杠
    String SYMBOL_BACKSLASH = "\\";                 // 反斜杠
    String SYMBOL_EQUAL = "=";                      // 等号
    String SYMBOL_PERCENT = "%";                    // 百分号
    String SYMBOL_AMPERSAND = "&";                  // 与符号
    String SYMBOL_PIPE = "|";                       // 管道符
    String SYMBOL_CARET = "^";                      // 脱字符
    String SYMBOL_TILDE = "~";                      // 波浪号
    String SYMBOL_AT = "@";                         // 艾特符
    String SYMBOL_HASH = "#";                       // 井号
    String SYMBOL_DOLLAR = "$";                     // 美元符

    /**
     * 常用符号 - 括号
     */
    String SYMBOL_LEFT_PAREN = "(";                 // 左小括号
    String SYMBOL_RIGHT_PAREN = ")";                // 右小括号
    String SYMBOL_LEFT_BRACKET = "[";               // 左中括号
    String SYMBOL_RIGHT_BRACKET = "]";              // 右中括号
    String SYMBOL_LEFT_BRACE = "{";                 // 左大括号
    String SYMBOL_RIGHT_BRACE = "}";                // 右大括号
    String SYMBOL_LESS_THAN = "<";                  // 小于号
    String SYMBOL_GREATER_THAN = ">";               // 大于号

    /**
     * 常用符号 - 其他
     */
    String SYMBOL_UNDERLINE = "_";                  // 下划线
}