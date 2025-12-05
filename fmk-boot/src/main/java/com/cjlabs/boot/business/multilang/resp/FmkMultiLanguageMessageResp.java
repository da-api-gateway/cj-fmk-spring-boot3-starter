package com.cjlabs.boot.business.multilang.resp;

import lombok.Data;

import java.time.Instant;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 02:39:34
 */
@Data
public class FmkMultiLanguageMessageResp {


    /**
     * Primary key ID; 主键ID
     */
    private Long id;

    /**
     * Message type; 消息类型
     */
    private String messageType;

    /**
     * Message key; 消息键
     */
    private String messageKey;

    /**
     * Language code (en, zh); 语言代码
     */
    private String languageCode;

    /**
     * Message content; 消息内容
     */
    private String messageValue;

    /**
     * Creator user ID; 创建用户ID
     */
    private String createUser;

    /**
     * Creation timestamp (UTC, milliseconds); 创建时间(UTC毫秒时间戳)
     */
    private Instant createDate;

    /**
     * Updater user ID or name; 更新用户ID或名称
     */
    private String updateUser;

    /**
     * Update timestamp (UTC, milliseconds); 更新时间(UTC毫秒时间戳)
     */
    private Instant updateDate;

    /**
     *
     */
    private String traceId;

}