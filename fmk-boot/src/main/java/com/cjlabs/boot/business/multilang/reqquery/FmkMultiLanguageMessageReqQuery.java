package com.cjlabs.boot.business.multilang.reqquery;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 02:39:34
 */
@Data
public class FmkMultiLanguageMessageReqQuery {

    /**
     * id
     */
    private Long id;

    /**
     * Message type; 消息类型
     */
    private String messageType;

    /**
     * Message type; 消息类型
     */
    private List<String> messageKeyList;

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