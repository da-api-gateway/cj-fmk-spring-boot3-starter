package com.cjlabs.web.req;

import lombok.Getter;
import lombok.Setter;

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
    // @Min(value = 1, message = "页码必须大于0")
    private Long current = 1L;

    /**
     * 每页大小
     */
    // @Min(value = 1, message = "每页大小必须大于0")
    // @Max(value = 500, message = "每页大小不能超过500")
    private Long size = 10L;


    private List<FmkOrderItem> orderItemList;

    /**
     * 是否查询所有
     */
    private Boolean searchAllFlag = false;

}
