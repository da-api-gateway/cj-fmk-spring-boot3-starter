// package com.cjlabs.message.tg.miniapp;
//
// import com.cjlabs.web.check.FmkCheckUtil;
// import com.cjlabs.web.json.FmkJacksonUtil;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
//
// @Slf4j
// @Component
// public class TgMiniAppCall {
//
//     @Autowired
//     private TgBotConfig tgBotConfig;
//
//     // Telegram 链接的基础URL
//     private static final String TELEGRAM_BASE_URL = "https://t.me/";
//
//     // 邀请链接的正则表达式
//     private static final Pattern INVITE_LINK_PATTERN = Pattern.compile(
//             "https://t\\.me/([^?]+)\\?start=([^&\\s]+)"
//     );
//
//     /**
//      * 构建 Mini App URL - 使用配置中的 URL
//      */
//     public String buildMiniAppUrl(String inviteCode) {
//         try {
//             // 使用配置中的 Mini App URL
//             String baseUrl = tgBotConfig.getMiniApp().getUrl();
//
//             StringBuilder urlBuilder = new StringBuilder(baseUrl);
//
//             // Telegram Web App 会自动注入这些参数，我们只需要添加自定义参数
//             boolean hasParams = false;
//
//             if (StringUtils.isNotBlank(inviteCode)) {
//                 urlBuilder.append("?");
//                 urlBuilder.append("invite_code=").append(inviteCode);
//                 hasParams = true;
//             }
//
//             // 添加时间戳防止缓存
//             urlBuilder.append(hasParams ? "&" : "?");
//             urlBuilder.append("t=").append(System.currentTimeMillis());
//
//             String finalUrl = urlBuilder.toString();
//             log.info("构建Mini App URL: {}", finalUrl);
//
//             return finalUrl;
//
//         } catch (Exception e) {
//             log.error("构建Mini App URL失败", e);
//             return tgBotConfig.getMiniApp().getUrl(); // 降级到基础URL
//         }
//     }
//
//     /**
//      * 生成邀请链接
//      *
//      * @param inviteCode 邀请码
//      * @return 完整的邀请链接
//      */
//     public String generateInviteLink(String inviteCode) {
//         FmkCheckUtil.checkInput(StringUtils.isBlank(inviteCode));
//         String botUsername = tgBotConfig.getBot().getUsername();
//         FmkCheckUtil.checkInput(StringUtils.isBlank(botUsername));
//         log.info("TgMiniAppCall|generateInviteLink|tgBotConfig.getBot()={}", FmkJacksonUtil.toJson(tgBotConfig.getBot()));
//         // 对邀请码进行URL编码，确保特殊字符正确处理
//         String encodedInviteCode = URLEncoder.encode(inviteCode, StandardCharsets.UTF_8);
//
//         String inviteLink = TELEGRAM_BASE_URL + botUsername + "?start=" + encodedInviteCode;
//
//         log.info("生成邀请链接 - Bot: {}, InviteCode: {}, Link: {}", botUsername, inviteCode, inviteLink);
//
//         return inviteLink;
//     }
//
//     /**
//      * 从邀请链接中解析邀请码
//      *
//      * @param inviteLink 邀请链接
//      * @return 邀请码，如果解析失败返回null
//      */
//     public String parseInviteCodeFromLink(String inviteLink) {
//         if (StringUtils.isBlank(inviteLink)) {
//             log.warn("邀请链接为空");
//             return null;
//         }
//
//         Matcher matcher = INVITE_LINK_PATTERN.matcher(inviteLink);
//         if (matcher.matches()) {
//             String botUsername = matcher.group(1);
//             String inviteCode = matcher.group(2);
//
//             log.info("解析邀请链接 - Bot: {}, InviteCode: {}, Link: {}",
//                     botUsername, inviteCode, inviteLink);
//
//             return inviteCode;
//         } else {
//             log.warn("邀请链接格式不正确: {}", inviteLink);
//             return null;
//         }
//     }
//
//     /**
//      * 验证邀请链接是否属于当前Bot
//      *
//      * @param inviteLink 邀请链接
//      * @return true if 链接属于当前Bot
//      */
//     public boolean isValidBotInviteLink(String inviteLink) {
//         if (StringUtils.isBlank(inviteLink)) {
//             return false;
//         }
//
//         Matcher matcher = INVITE_LINK_PATTERN.matcher(inviteLink);
//         if (matcher.matches()) {
//             String botUsername = matcher.group(1);
//             String currentBotUsername = tgBotConfig.getBot().getUsername();
//
//             boolean isValid = StringUtils.equals(botUsername, currentBotUsername);
//
//             log.info("验证邀请链接 - Link: {}, CurrentBot: {}, LinkBot: {}, Valid: {}",
//                     inviteLink, currentBotUsername, botUsername, isValid);
//
//             return isValid;
//         }
//
//         return false;
//     }
//
//     /**
//      * 生成带过期时间的邀请链接（在邀请码中编码时间戳）
//      *
//      * @param baseInviteCode   基础邀请码
//      * @param expireTimeMillis 过期时间（毫秒时间戳）
//      * @return 带过期时间的邀请链接
//      */
//     public String generateTimeLimitedInviteLink(String baseInviteCode, long expireTimeMillis) {
//         if (StringUtils.isBlank(baseInviteCode)) {
//             throw new IllegalArgumentException("基础邀请码不能为空");
//         }
//
//         // 将过期时间编码到邀请码中
//         String timeLimitedCode = baseInviteCode + "_" + expireTimeMillis;
//
//         return generateInviteLink(timeLimitedCode);
//     }
//
// //    /**
// //     * 解析限时邀请码，检查是否过期
// //     *
// //     * @param timeLimitedInviteCode 限时邀请码
// //     * @return InviteCodeInfo 包含基础邀请码和是否过期的信息
// //     */
// //    public InviteCodeInfo parseTimeLimitedInviteCode(String timeLimitedInviteCode) {
// //        if (StringUtils.isBlank(timeLimitedInviteCode)) {
// //            return new InviteCodeInfo(null, true, "邀请码为空");
// //        }
// //
// //        // 检查是否包含时间戳
// //        if (!timeLimitedInviteCode.contains("_")) {
// //            // 普通邀请码，不过期
// //            return new InviteCodeInfo(timeLimitedInviteCode, false, null);
// //        }
// //
// //        String[] parts = timeLimitedInviteCode.split("_");
// //        if (parts.length < 2) {
// //            return new InviteCodeInfo(timeLimitedInviteCode, false, null);
// //        }
// //
// //        String baseCode = parts[0];
// //        String timeStr = parts[parts.length - 1];
// //
// //        try {
// //            long expireTime = Long.parseLong(timeStr);
// //            long currentTime = System.currentTimeMillis();
// //
// //            boolean isExpired = currentTime > expireTime;
// //            String errorMsg = isExpired ? "邀请链接已过期" : null;
// //
// //            log.info("解析限时邀请码 - Code: {}, BaseCode: {}, ExpireTime: {}, CurrentTime: {}, Expired: {}",
// //                    timeLimitedInviteCode, baseCode, expireTime, currentTime, isExpired);
// //
// //            return new InviteCodeInfo(baseCode, isExpired, errorMsg);
// //
// //        } catch (NumberFormatException e) {
// //            // 时间戳格式错误，当作普通邀请码处理
// //            return new InviteCodeInfo(timeLimitedInviteCode, false, null);
// //        }
// //    }
//
//     /**
//      * 获取当前Bot的基础链接
//      *
//      * @return Bot基础链接 (例如: https://t.me/Sheraabbccbot)
//      */
//     public String getBotBaseLink() {
//         String botUsername = tgBotConfig.getBot().getUsername();
//         if (StringUtils.isBlank(botUsername)) {
//             throw new IllegalStateException("Bot用户名未配置");
//         }
//         return TELEGRAM_BASE_URL + botUsername;
//     }
// }
