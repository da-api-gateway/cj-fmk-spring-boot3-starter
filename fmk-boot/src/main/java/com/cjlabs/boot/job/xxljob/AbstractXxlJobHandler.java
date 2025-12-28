package com.cjlabs.boot.job.xxljob;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import static com.cjlabs.domain.common.FmkConstant.MDC_TRACE_ID;

/**
 * XXL-Job Handler 抽象基类
 * <p>
 * 封装通用逻辑：
 * 1. 自动生成和设置 traceId
 * 2. 统一的日志格式
 * 3. 统一的异常处理
 * 4. 统一的执行时间统计
 * 5. MDC 上下文管理
 * <p>
 * 子类只需实现 doExecute() 方法，专注业务逻辑
 */
@Slf4j
public abstract class AbstractXxlJobHandler extends IJobHandler {

    @Override
    public final void execute() throws Exception {
        // 获取 XXL-Job 上下文信息
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        long jobId = XxlJobHelper.getJobId();
        String jobParam = XxlJobHelper.getJobParam();

        // 生成 traceId
        FmkTraceId traceId = FmkTraceId.generate();

        long startTime = System.currentTimeMillis();

        // 创建执行上下文
        JobExecutionContext context = new JobExecutionContext(
                jobId, shardIndex, shardTotal, jobParam, traceId
        );

        int processedCount = 0;

        // 设置 MDC 上下文（用于日志追踪）
        MDC.put(MDC_TRACE_ID, traceId.getValue());

        try {
            // 记录任务启动日志
            logJobStart(context);

            // 执行子类的业务逻辑
            processedCount = doExecute(context);

            // 记录任务成功日志
            long duration = System.currentTimeMillis() - startTime;
            logJobSuccess(context, processedCount, duration);

            // 通知 XXL-Job 执行成功
            handleSuccess(context, processedCount, duration);

        } catch (Exception e) {
            // 记录任务失败日志
            long duration = System.currentTimeMillis() - startTime;
            logJobFailure(context, processedCount, duration, e);

            // 通知 XXL-Job 执行失败
            handleFailure(context, processedCount, duration, e);

            // 重新抛出异常（如果需要）
            throw e;
        } finally {
            // 清理 MDC 上下文
            MDC.remove(MDC_TRACE_ID);
        }
    }

    /**
     * 子类实现此方法，执行具体的业务逻辑
     *
     * @param context 执行上下文
     * @return 处理的任务数量
     * @throws Exception 执行异常
     */
    protected abstract int doExecute(JobExecutionContext context) throws Exception;

    /**
     * 记录任务启动日志
     */
    protected void logJobStart(JobExecutionContext context) {
        log.info("任务启动 | 任务名称: {} | JobId: {} | 分片: {}/{} | TraceId: {} | 参数: {}",
                getJobName(), context.getJobId(), context.getShardIndex(), context.getShardTotal(),
                context.getTraceId().getValue(), context.getJobParam());
    }

    /**
     * 记录任务成功日志
     */
    protected void logJobSuccess(JobExecutionContext context, int processedCount, long duration) {
        double throughput = duration > 0 ? (processedCount * 1000.0 / duration) : 0;

        log.info("任务成功 | 任务名称: {} | 分片: {}/{} | 处理数量: {} | 耗时: {} ms | 速率: {} 条/秒 | TraceId: {}",
                getJobName(), context.getShardIndex(), context.getShardTotal(),
                processedCount, duration, String.format("%.0f", throughput), context.getTraceId().getValue());
    }


    /**
     * 记录任务失败日志
     */
    protected void logJobFailure(JobExecutionContext context, int processedCount, long duration, Exception e) {
        log.error("任务失败 | 任务名称: {} | 分片: {}/{} | 已处理: {} | 耗时: {} ms | 错误: {} | TraceId: {}",
                getJobName(), context.getShardIndex(), context.getShardTotal(),
                processedCount, duration, e.getMessage(), context.getTraceId().getValue(), e);
    }

    /**
     * 处理成功
     */
    protected void handleSuccess(JobExecutionContext context, int processedCount, long duration) {
        double throughput = duration > 0 ? (processedCount * 1000.0 / duration) : 0;

        String successMsg = String.format(
                "[%s] 分片 %d/%d 完成: 处理 %d 条，耗时 %d ms，速率 %.0f 条/秒",
                getJobName(),
                context.getShardIndex(),
                context.getShardTotal(),
                processedCount,
                duration,
                throughput
        );

        XxlJobHelper.handleSuccess(successMsg);
    }

    /**
     * 处理失败
     */
    protected void handleFailure(JobExecutionContext context, int processedCount, long duration, Exception e) {
        String errorMsg = String.format(
                "[%s] 分片 %d/%d 异常: 已处理 %d 条，耗时 %d ms，错误: %s",
                getJobName(),
                context.getShardIndex(),
                context.getShardTotal(),
                processedCount,
                duration,
                e.getMessage()
        );

        XxlJobHelper.handleFail(errorMsg);
    }

    /**
     * 获取任务名称（用于日志）
     * 子类可以覆盖此方法返回自定义名称
     */
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

}