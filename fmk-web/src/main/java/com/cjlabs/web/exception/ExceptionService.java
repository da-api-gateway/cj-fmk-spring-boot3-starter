package com.cjlabs.web.exception;

import com.xodo.business.common.multilanguage.service.MultiLanguageMessageService;
import com.xodo.fmk.common.LanguageEnum;
import com.xodo.fmk.core.enums.IEnumStrV2;
import com.xodo.fmk.web.FmkContextUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ExceptionService {
    @Autowired
    private MultiLanguageMessageService multiLanguageMessageService;

    public String errorMsg(String msgType, String msgKey) {
        LanguageEnum language = FmkContextUtil.getCurrentLanguageCode();

        if (StringUtils.isBlank(msgType) || StringUtils.isBlank(msgKey)) {
            return getEnumMessageByLang(MultiLanguageMessageExceptionEnum.SYSTEM_ERROR___SERVER_ERROR, language);
        }

        // 优先使用枚举定义（多用于固定错误）
        Optional<MultiLanguageMessageExceptionEnum> exceptionEnum = IEnumStrV2.getEnumByCode(msgType, msgKey, MultiLanguageMessageExceptionEnum.class);
        if (exceptionEnum.isPresent()) {
            return getEnumMessageByLang(exceptionEnum.get(), language);
        }

        // 查找缓存中模板
        Map<LanguageEnum, String> messageMap = multiLanguageMessageService.getByTypeAndKeyCache(msgType, msgKey);
        if (MapUtils.isEmpty(messageMap)) {
            return getEnumMessageByLang(MultiLanguageMessageExceptionEnum.SYSTEM_ERROR___SERVER_ERROR, language);
        }

        String errorMsg = messageMap.get(language);

        return Optional.ofNullable(errorMsg)
                .orElse(getEnumMessageByLang(MultiLanguageMessageExceptionEnum.SYSTEM_ERROR___SERVER_ERROR, language));
    }

    public String commonErrorMsg(String zhMsg, String enMsg) {
        LanguageEnum languageCode = FmkContextUtil.getCurrentLanguageCode();
        if (LanguageEnum.ZH.equals(languageCode)) {
            return zhMsg;
        } else {
            return enMsg;
        }
    }

    private String getEnumMessageByLang(MultiLanguageMessageExceptionEnum exceptionEnum, LanguageEnum language) {
        if (LanguageEnum.ZH.equals(language)) {
            return exceptionEnum.getMsgZh();
        } else {
            return exceptionEnum.getMsgEn();
        }
    }
}
