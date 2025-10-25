package com.cjlabs.db.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class FmkRequest<T> {

    private String businessKey;

    private List<String> businessKeyList;

    private T request;

    /**
     * 当前页码，从1开始
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;

    private List<FmkOrderItem> orderItemList;

    /**
     * 是否查询所有
     */
    private Boolean searchAllFlag = false;

}
