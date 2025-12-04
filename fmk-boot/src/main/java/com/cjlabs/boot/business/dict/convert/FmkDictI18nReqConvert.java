package com.cjlabs.boot.business.dict.convert;

import com.cjlabs.boot.business.dict.mysql.FmkDictI18n;
import com.cjlabs.boot.business.dict.reqquery.FmkDictI18nReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictI18nReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictI18nReqUpdate;

import java.util.Objects;

public class FmkDictI18nReqConvert {

    public static FmkDictI18n toDb(FmkDictI18nReqQuery input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDictI18n fmkDictI18n = new FmkDictI18n();

        fmkDictI18n.setDictType(input.getDictType());
        fmkDictI18n.setDictKey(input.getDictKey());

        return fmkDictI18n;
    }

    public static FmkDictI18n toDb(FmkDictI18nReqUpdate input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDictI18n fmkDictI18n = new FmkDictI18n();

        fmkDictI18n.setDictType(input.getDictType());
        fmkDictI18n.setDictKey(input.getDictKey());
        fmkDictI18n.setLanguageCode(input.getLanguageCode());
        fmkDictI18n.setDictValue(input.getDictValue());
        fmkDictI18n.setRemark(input.getRemark());

        return fmkDictI18n;
    }

    public static FmkDictI18n toDb(FmkDictI18nReqSave input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDictI18n fmkDictI18n = new FmkDictI18n();

        fmkDictI18n.setDictType(input.getDictType());
        fmkDictI18n.setDictKey(input.getDictKey());
        fmkDictI18n.setLanguageCode(input.getLanguageCode());
        fmkDictI18n.setDictValue(input.getDictValue());
        fmkDictI18n.setRemark(input.getRemark());

        return fmkDictI18n;
    }
}