package com.cjlabs.boot.business.multilang.mysql;

import com.cjlabs.db.domain.FmkBaseEntity;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 02:39:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FmkMultiLanguageMessage extends FmkBaseEntity {

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

}