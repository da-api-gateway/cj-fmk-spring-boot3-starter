package com.cjlabs.db.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class IPageConvert {

    public static <T1, T2> IPage<T2> convert(IPage<T1> source, List<T2> t2List) {
        if (source == null) {
            return null;
        }
        IPage<T2> t2IPage = new Page<>(source.getCurrent(), source.getSize());
        t2IPage.setRecords(t2List);
        t2IPage.setTotal(source.getTotal());
        return t2IPage;
    }
}
