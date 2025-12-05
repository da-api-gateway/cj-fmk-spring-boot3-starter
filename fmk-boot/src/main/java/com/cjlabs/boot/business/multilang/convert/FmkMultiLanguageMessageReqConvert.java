package com.cjlabs.boot.business.multilang.convert;

import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.reqquery.FmkMultiLanguageMessageReqQuery;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqSave;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqUpdate;

import java.util.Objects;

public class FmkMultiLanguageMessageReqConvert {

    public static FmkMultiLanguageMessage toDb(FmkMultiLanguageMessageReqQuery input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkMultiLanguageMessage fmkMultiLanguageMessage = new FmkMultiLanguageMessage();

        // fmkMultiLanguageMessage.setId(input.getId());
        fmkMultiLanguageMessage.setMessageType(input.getMessageType());
        fmkMultiLanguageMessage.setMessageKey(input.getMessageKey());
        fmkMultiLanguageMessage.setLanguageCode(input.getLanguageCode());
        fmkMultiLanguageMessage.setMessageValue(input.getMessageValue());

        return fmkMultiLanguageMessage;
    }

    public static FmkMultiLanguageMessage toDb(FmkMultiLanguageMessageReqUpdate input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkMultiLanguageMessage fmkMultiLanguageMessage = new FmkMultiLanguageMessage();

        fmkMultiLanguageMessage.setId(input.getId());
        fmkMultiLanguageMessage.setMessageType(input.getMessageType());
        fmkMultiLanguageMessage.setMessageKey(input.getMessageKey());
        fmkMultiLanguageMessage.setLanguageCode(input.getLanguageCode());
        fmkMultiLanguageMessage.setMessageValue(input.getMessageValue());

        return fmkMultiLanguageMessage;
    }

    public static FmkMultiLanguageMessage toDb(FmkMultiLanguageMessageReqSave input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkMultiLanguageMessage fmkMultiLanguageMessage = new FmkMultiLanguageMessage();

        fmkMultiLanguageMessage.setMessageType(input.getMessageType());
        fmkMultiLanguageMessage.setMessageKey(input.getMessageKey());
        fmkMultiLanguageMessage.setLanguageCode(input.getLanguageCode());
        fmkMultiLanguageMessage.setMessageValue(input.getMessageValue());

        return fmkMultiLanguageMessage;
    }
}