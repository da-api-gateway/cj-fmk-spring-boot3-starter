package com.cjlabs.boot.business.multilang.requpdate;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.Data;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 02:39:34
 */
@Data
public class FmkMultiLanguageMessageReqUpdate {

    /**
     * ID（更新时必填）
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
    private FmkLanguageEnum languageCode;

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
    private Long createDate;

    /**
     * Updater user ID or name; 更新用户ID或名称
     */
    private String updateUser;

}