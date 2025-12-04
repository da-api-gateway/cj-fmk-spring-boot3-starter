package com.cjlabs.boot.business.dict.convert;

import com.cjlabs.boot.business.dict.mysql.FmkDict;
import com.cjlabs.boot.business.dict.reqquery.FmkDictReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictReqUpdate;

import java.util.Objects;

public class FmkDictReqConvert {

    public static FmkDict toDb(FmkDictReqQuery input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDict fmkDict = new FmkDict();

        fmkDict.setId(input.getId());
        fmkDict.setDictType(input.getDictType());
        fmkDict.setStatus(input.getStatus());
        fmkDict.setRemark(input.getRemark());

        return fmkDict;
    }

    public static FmkDict toDb(FmkDictReqUpdate input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDict fmkDict = new FmkDict();

        fmkDict.setId(input.getId());
        fmkDict.setDictType(input.getDictType());
        fmkDict.setStatus(input.getStatus());
        fmkDict.setRemark(input.getRemark());

        return fmkDict;
    }

    public static FmkDict toDb(FmkDictReqSave input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDict fmkDict = new FmkDict();

        // fmkDict.setId(input.getId());
        fmkDict.setDictType(input.getDictType());
        fmkDict.setStatus(input.getStatus());
        fmkDict.setRemark(input.getRemark());

        return fmkDict;
    }
}