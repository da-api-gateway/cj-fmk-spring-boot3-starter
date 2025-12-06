// package com.cjlabs.message.tg.config;
//
// import com.cjlabs.tg.enums.TelegramApiMethodEnum;
// import com.cjlabs.web.json.FmkJacksonUtil;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.context.properties.NestedConfigurationProperty;
// import org.springframework.stereotype.Component;
//
// @Slf4j
// @Getter
// @Setter
// @Component
// @ConfigurationProperties(prefix = "xodo")
// public class TgBotConfig implements InitializingBean {
//
//     /**
//      * 是否启用整个应用
//      */
//     private boolean enabled = true;
//
//     /**
//      * 应用名称
//      */
//     private String appName;
//
//     /**
//      * Telegram Bot 配置
//      */
//     @NestedConfigurationProperty
//     private TelegramBot bot = new TelegramBot();
//
//     /**
//      * Mini App 配置
//      */
//     @NestedConfigurationProperty
//     private MiniApp miniApp = new MiniApp();
//
//     /**
//      * 获取完整的 Telegram API URL (使用枚举)
//      */
//     public String getApiFullUrl(TelegramApiMethodEnum method) {
//         return bot.apiUrl + "/bot" + bot.token + "/" + method.getCode();
//     }
//
//     /**
//      * 检查应用是否完整配置
//      */
//     public boolean isFullyConfigured() {
//         return enabled
//                 && StringUtils.isNotBlank(bot.getToken())
//                 && StringUtils.isNotBlank(miniApp.getUrl());
//     }
//
//     /**
//      * 检查是否为管理员
//      */
//     public boolean checkAdmin(Long userId) {
//         if (bot.adminUserIds == null || userId == null) {
//             return false;
//         }
//         for (Long adminId : bot.adminUserIds) {
//             if (adminId.equals(userId)) {
//                 return true;
//             }
//         }
//         return false;
//     }
//
//     /**
//      * 验证API方法是否需要管理员权限
//      */
//     public boolean requiresAdminPermission(TelegramApiMethodEnum method, Long userId) {
//         return method.requiresAdminPermission() && !checkAdmin(userId);
//     }
//
//     @Override
//     public void afterPropertiesSet() throws Exception {
//         log.info("TgBotConfig|afterPropertiesSet|bot={}", FmkJacksonUtil.toJson(bot));
//         log.info("TgBotConfig|afterPropertiesSet|miniApp={}", FmkJacksonUtil.toJson(miniApp));
//     }
//
//     /**
//      * Telegram Bot 配置类
//      */
//     @Getter
//     @Setter
//     public static class TelegramBot {
//         /**
//          * Bot 名称
//          */
//         private String name;
//
//         /**
//          * Bot 用户名
//          */
//         private String username;
//
//         /**
//          * Bot Token
//          */
//         private String token;
//
//         /**
//          * Telegram API 基础 URL
//          */
//         private String apiUrl = "https://api.telegram.org";
//
//         /**
//          * 超时时间（秒）
//          */
//         private Integer timeout = 30;
//
//         /**
//          * 长轮询超时时间（秒）
//          */
//         private Integer longPollingTimeout = 50;
//
//         /**
//          * 是否启用日志
//          */
//         private boolean enableLogging = true;
//
//         /**
//          * 管理员用户ID列表
//          */
//         private Long[] adminUserIds = {};
//
//         /**
//          * 是否启用自动欢迎
//          */
//         private boolean autoWelcome = true;
//
//         /**
//          * 支持的命令列表
//          */
//         private String[] supportedCommands = {"/start", "/help", "/status"};
//     }
//
//     /**
//      * Mini App 配置类
//      */
//     @Getter
//     @Setter
//     public static class MiniApp {
//         /**
//          * Mini App URL
//          */
//         private String url;
//
//         /**
//          * Mini App 名称
//          */
//         private String name;
//
//         /**
//          * Mini App 图标 URL
//          */
//         private String iconUrl;
//
//         /**
//          * Mini App 描述
//          */
//         private String description;
//     }
// }