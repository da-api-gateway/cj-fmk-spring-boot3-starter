package com.cjlabs.message.tg.chat;//package com.cjlabs.tg;
//
//import com.cjlabs.core.http.jdk21.FmkJdkHttpClientUtil;
//import com.cjlabs.tg.enums.TelegramApiMethodEnum;
//import com.cjlabs.tg.req.SendMessageRequest;
//import com.cjlabs.tg.resp.SentMessage;
//import com.cjlabs.tg.resp.TelegramApiResponse;
//import com.cjlabs.web.check.FmkCheckUtil;
//import com.cjlabs.web.exception.BusinessExceptionEnum;
//import com.cjlabs.web.json.FmkJacksonUtil;
//import com.cjlabs.web.threadlocal.FmkResult;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.ParseMode;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Slf4j
//@Component
//public class TgSendMsgCall {
//
//    @Autowired
//    private TgBotConfig tgBotConfig;
//
//    // 全局限流计数器 - Telegram API 全局限制为每秒约30个请求
//    private final AtomicInteger globalRequestCounter = new AtomicInteger(0);
//
//    // 每个聊天ID的限流计数器 - Telegram API 对单个聊天限制为每秒约1个消息
//    private final Map<Long, AtomicInteger> chatRequestCounters = new ConcurrentHashMap<>();
//
//    // 限流配置
//    private static final int GLOBAL_RATE_LIMIT = 25; // 全局每秒最大请求数（保守设置）
//    private static final int CHAT_RATE_LIMIT = 1;    // 每个聊天ID每秒最大请求数
//    private static final Duration RATE_LIMIT_WINDOW = Duration.ofSeconds(1); // 限流窗口
//
//    // 定时任务执行器，用于重置计数器
//    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//    public TgSendMsgCall() {
//        // 每秒重置计数器
//        scheduler.scheduleAtFixedRate(this::resetRateLimiters,
//                RATE_LIMIT_WINDOW.toMillis(), RATE_LIMIT_WINDOW.toMillis(), TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 重置所有限流计数器
//     */
//    private void resetRateLimiters() {
//        globalRequestCounter.set(0);
//        chatRequestCounters.clear();
//    }
//
//    /**
//     * 检查是否超过限流阈值
//     *
//     * @param chatId 聊天ID
//     * @return 如果未超过限流返回true，否则返回false
//     */
//    private boolean checkRateLimit(Long chatId) {
//        // 检查全局限流
//        if (globalRequestCounter.incrementAndGet() > GLOBAL_RATE_LIMIT) {
//            log.warn("全局API请求限流触发，当前请求数: {}", globalRequestCounter.get());
//            return false;
//        }
//
//        // 检查聊天ID限流
//        if (chatId != null) {
//            AtomicInteger chatCounter = chatRequestCounters.computeIfAbsent(chatId, k -> new AtomicInteger(0));
//            if (chatCounter.incrementAndGet() > CHAT_RATE_LIMIT) {
//                log.warn("聊天ID: {} 请求限流触发，当前请求数: {}", chatId, chatCounter.get());
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 处理限流逻辑，如果被限流则等待并重试
//     *
//     * @param chatId     聊天ID
//     * @param retryCount 当前重试次数
//     * @return 是否可以继续请求
//     */
//    private boolean handleRateLimit(Long chatId, int retryCount) {
//        if (!checkRateLimit(chatId)) {
//            if (retryCount < 3) { // 最多重试3次
//                try {
//                    long backoffTime = (long) (Math.pow(2, retryCount) * 100); // 指数退避
//                    log.info("API请求被限流，等待 {} ms 后重试，聊天ID: {}, 重试次数: {}", backoffTime, chatId, retryCount + 1);
//                    Thread.sleep(backoffTime);
//                    return handleRateLimit(chatId, retryCount + 1);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    return false;
//                }
//            } else {
//                log.error("API请求被限流且重试次数已达上限，聊天ID: {}", chatId);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 发送普通文本消息
//     *
//     * @param chatId 聊天ID
//     * @param text   消息文本
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendTextMessage(Long chatId, String text) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(text);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送HTML格式的消息
//     *
//     * @param chatId 聊天ID
//     * @param html   HTML格式文本
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendHtmlMessage(Long chatId, String html) {
//        if (!handleRateLimit(chatId, 0)) {
////            return FmkResult.error(HttpStatus.TOO_MANY_REQUESTS.value(), "发送消息失败：请求被限流");
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(html);
//        request.setParseMode(ParseMode.HTML);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送Markdown格式的消息
//     *
//     * @param chatId   聊天ID
//     * @param markdown Markdown格式文本
//     * @param v2       是否使用MarkdownV2格式
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendMarkdownMessage(Long chatId, String markdown, boolean v2) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(markdown);
//        request.setParseMode(v2 ? ParseMode.MARKDOWNV2 : ParseMode.MARKDOWN);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送回复消息
//     *
//     * @param chatId           聊天ID
//     * @param text             消息文本
//     * @param replyToMessageId 回复的消息ID
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendReplyMessage(Long chatId, String text, Long replyToMessageId) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(text);
//        request.setReplyToMessageId(replyToMessageId);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送静默消息（不会触发通知声音）
//     *
//     * @param chatId 聊天ID
//     * @param text   消息文本
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendSilentMessage(Long chatId, String text) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(text);
//        request.setDisableNotification(true);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送受保护内容的消息（防止转发）
//     *
//     * @param chatId 聊天ID
//     * @param text   消息文本
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendProtectedMessage(Long chatId, String text) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(text);
//        request.setProtectContent(true);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送无预览链接的消息（链接不会生成预览）
//     *
//     * @param chatId 聊天ID
//     * @param text   包含链接的消息文本
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendMessageWithoutLinkPreview(Long chatId, String text) {
//        if (!handleRateLimit(chatId, 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        SendMessageRequest request = new SendMessageRequest();
//        request.setChatId(chatId);
//        request.setText(text);
//        request.setDisableWebPagePreview(true);
//        return sendMessage(request);
//    }
//
//    /**
//     * 发送普通消息
//     *
//     * @param request 消息请求
//     * @return 发送结果
//     */
//    public FmkResult<SentMessage> sendMessage(SendMessageRequest request) {
//        // 检查限流
//        if (!handleRateLimit(request.getChatId(), 0)) {
//            FmkCheckUtil.throwBusiness(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
//        }
//
//        try {
//            // 使用枚举
//            String apiUrl = tgBotConfig.getApiFullUrl(TelegramApiMethodEnum.SEND_MESSAGE);
//            String requestJson = FmkJacksonUtil.toJson(request);
//
//
//            log.info("发送消息 - Method: {}, URL: {}, Request: {}",
//                    TelegramApiMethodEnum.SEND_MESSAGE.getMsg(), apiUrl, requestJson);
//
//            String responseStr = FmkJdkHttpClientUtil.postJson(apiUrl, requestJson);
//            log.info("发送消息响应: {}", responseStr);
//
//            TelegramApiResponse<SentMessage> response = FmkJacksonUtil.parseObj(responseStr, new TypeReference<>() {
//            });
//
//            if (response != null && response.isOk() && response.getResult() != null) {
//                return FmkResult.success(response.getResult());
//            } else {
//                String errorMsg = response != null && response.getDescription() != null ? response.getDescription() : "发送消息失败";
////                return FmkResult.error();
//            }
//
//        } catch (Exception e) {
//            log.error("发送消息失败", e);
////            return FmkResult.error();
//        }
//        return null;
//    }
//
//}
