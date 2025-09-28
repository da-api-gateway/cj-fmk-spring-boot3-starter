package com.cjlabs.db.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页响应结果
 */
@Data
public class FmkPageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否为第一页
     */
    private Boolean isFirst;

    /**
     * 是否为最后一页
     */
    private Boolean isLast;

    /**
     * 是否为空
     */
    private Boolean isEmpty;

    public FmkPageResponse() {
    }

    public FmkPageResponse(List<T> records, Long total, Long current, Long size, Long pages) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = pages;
        this.hasNext = current < pages;
        this.hasPrevious = current > 1;
        this.isFirst = current == 1;
        this.isLast = current.equals(pages);
        this.isEmpty = records == null || records.isEmpty();
    }

    /**
     * 从MyBatis-Plus的IPage创建
     */
    public static <T> FmkPageResponse<T> of(IPage<T> page) {
        if (page == null) {
            return empty();
        }

        return new FmkPageResponse<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages()
        );
    }

    /**
     * 从MyBatis-Plus的IPage创建并转换数据类型
     */
    public static <T, R> FmkPageResponse<R> of(IPage<T> page, Function<T, R> mapper) {
        if (page == null) {
            return empty();
        }

        List<R> mappedRecords = page.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new FmkPageResponse<>(
                mappedRecords,
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages()
        );
    }

    /**
     * 创建空的分页结果
     */
    public static <T> FmkPageResponse<T> empty() {
        return new FmkPageResponse<>(Lists.newArrayList(), 0L, 1L, 10L, 0L);
    }

    /**
     * 创建空的分页结果（指定分页参数）
     */
    public static <T> FmkPageResponse<T> empty(Long current, Long size) {
        return new FmkPageResponse<>(Lists.newArrayList(), 0L, current, size, 0L);
    }
}