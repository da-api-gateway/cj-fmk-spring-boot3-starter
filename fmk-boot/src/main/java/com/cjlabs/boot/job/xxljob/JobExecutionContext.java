package com.cjlabs.boot.job.xxljob;

import com.cjlabs.core.types.strings.FmkTraceId;
import lombok.Getter;

/**
 * 任务执行上下文
 */
@Getter
public class JobExecutionContext {
    /**
     * 任务 ID
     */
    private final long jobId;

    /**
     * 分片索引（从 0 开始）
     */
    private final int shardIndex;

    /**
     * 分片总数
     */
    private final int shardTotal;

    /**
     * 任务参数
     */
    private final String jobParam;

    /**
     * 链路追踪 ID
     */
    private final FmkTraceId traceId;

    public JobExecutionContext(long jobId,
                               int shardIndex,
                               int shardTotal,
                               String jobParam,
                               FmkTraceId traceId) {
        this.jobId = jobId;
        this.shardIndex = shardIndex;
        this.shardTotal = shardTotal;
        this.jobParam = jobParam;
        this.traceId = traceId;
    }

    /**
     * 是否是分片任务
     */
    public boolean isSharded() {
        return shardTotal > 1;
    }

    /**
     * 是否是第一个分片
     */
    public boolean isFirstShard() {
        return shardIndex == 0;
    }

    /**
     * 是否是最后一个分片
     */
    public boolean isLastShard() {
        return shardIndex == shardTotal - 1;
    }
}