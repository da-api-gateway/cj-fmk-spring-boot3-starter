package com.cjlabs.message.tg.enums;

import com.cjlabs.domain.enums.IEnumStr;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Telegram Bot API 方法枚举
 * 定义所有支持的 API 方法
 */
@Getter
@AllArgsConstructor
public enum TelegramApiMethodEnum implements IEnumStr {

    // 消息相关方法
    SEND_MESSAGE("sendMessage", "发送消息"),
    EDIT_MESSAGE_TEXT("editMessageText", "编辑消息文本"),
    EDIT_MESSAGE_REPLY_MARKUP("editMessageReplyMarkup", "编辑消息键盘"),
    DELETE_MESSAGE("deleteMessage", "删除消息"),
    FORWARD_MESSAGE("forwardMessage", "转发消息"),
    COPY_MESSAGE("copyMessage", "复制消息"),

    // 媒体消息方法
    SEND_PHOTO("sendPhoto", "发送图片"),
    SEND_AUDIO("sendAudio", "发送音频"),
    SEND_DOCUMENT("sendDocument", "发送文档"),
    SEND_VIDEO("sendVideo", "发送视频"),
    SEND_ANIMATION("sendAnimation", "发送动画"),
    SEND_VOICE("sendVoice", "发送语音"),
    SEND_VIDEO_NOTE("sendVideoNote", "发送视频消息"),
    SEND_MEDIA_GROUP("sendMediaGroup", "发送媒体组"),
    SEND_LOCATION("sendLocation", "发送位置"),
    SEND_VENUE("sendVenue", "发送场所"),
    SEND_CONTACT("sendContact", "发送联系人"),
    SEND_POLL("sendPoll", "发送投票"),
    SEND_DICE("sendDice", "发送骰子"),

    // 回调和交互方法
    ANSWER_CALLBACK_QUERY("answerCallbackQuery", "回答回调查询"),
    ANSWER_INLINE_QUERY("answerInlineQuery", "回答内联查询"),

    // 聊天操作方法
    GET_CHAT("getChat", "获取聊天信息"),
    GET_CHAT_ADMINISTRATORS("getChatAdministrators", "获取聊天管理员"),
    GET_CHAT_MEMBER_COUNT("getChatMemberCount", "获取聊天成员数量"),
    GET_CHAT_MEMBER("getChatMember", "获取聊天成员"),
    SET_CHAT_TITLE("setChatTitle", "设置聊天标题"),
    SET_CHAT_DESCRIPTION("setChatDescription", "设置聊天描述"),
    SET_CHAT_PHOTO("setChatPhoto", "设置聊天头像"),
    DELETE_CHAT_PHOTO("deleteChatPhoto", "删除聊天头像"),
    PIN_CHAT_MESSAGE("pinChatMessage", "置顶聊天消息"),
    UNPIN_CHAT_MESSAGE("unpinChatMessage", "取消置顶聊天消息"),
    UNPIN_ALL_CHAT_MESSAGES("unpinAllChatMessages", "取消置顶所有聊天消息"),
    LEAVE_CHAT("leaveChat", "离开聊天"),

    // 成员管理方法
    KICK_CHAT_MEMBER("kickChatMember", "踢出聊天成员"),
    UNBAN_CHAT_MEMBER("unbanChatMember", "解封聊天成员"),
    RESTRICT_CHAT_MEMBER("restrictChatMember", "限制聊天成员"),
    PROMOTE_CHAT_MEMBER("promoteChatMember", "提升聊天成员"),

    // Bot 信息方法
    GET_ME("getMe", "获取Bot信息"),
    GET_MY_COMMANDS("getMyCommands", "获取Bot命令"),
    SET_MY_COMMANDS("setMyCommands", "设置Bot命令"),
    DELETE_MY_COMMANDS("deleteMyCommands", "删除Bot命令"),

    // 更新获取方法
    GET_UPDATES("getUpdates", "获取更新"),
    SET_WEBHOOK("setWebhook", "设置Webhook"),
    DELETE_WEBHOOK("deleteWebhook", "删除Webhook"),
    GET_WEBHOOK_INFO("getWebhookInfo", "获取Webhook信息"),

    // 文件操作方法
    GET_FILE("getFile", "获取文件"),

    // 游戏相关方法
    SEND_GAME("sendGame", "发送游戏"),
    SET_GAME_SCORE("setGameScore", "设置游戏分数"),
    GET_GAME_HIGH_SCORES("getGameHighScores", "获取游戏高分"),

    // 支付相关方法
    SEND_INVOICE("sendInvoice", "发送发票"),
    ANSWER_SHIPPING_QUERY("answerShippingQuery", "回答运费查询"),
    ANSWER_PRE_CHECKOUT_QUERY("answerPreCheckoutQuery", "回答预结账查询"),

    // 贴纸相关方法
    SEND_STICKER("sendSticker", "发送贴纸"),
    GET_STICKER_SET("getStickerSet", "获取贴纸包"),
    UPLOAD_STICKER_FILE("uploadStickerFile", "上传贴纸文件"),
    CREATE_NEW_STICKER_SET("createNewStickerSet", "创建新贴纸包"),
    ADD_STICKER_TO_SET("addStickerToSet", "添加贴纸到贴纸包"),
    SET_STICKER_POSITION_IN_SET("setStickerPositionInSet", "设置贴纸在贴纸包中的位置"),
    DELETE_STICKER_FROM_SET("deleteStickerFromSet", "从贴纸包中删除贴纸"),
    SET_STICKER_SET_THUMB("setStickerSetThumb", "设置贴纸包缩略图");

    private final String code;
    private final String msg;

    /**
     * 检查是否为消息发送相关方法
     */
    public boolean isMessageMethod() {
        return this == SEND_MESSAGE ||
                this == SEND_PHOTO ||
                this == SEND_AUDIO ||
                this == SEND_DOCUMENT ||
                this == SEND_VIDEO ||
                this == SEND_ANIMATION ||
                this == SEND_VOICE ||
                this == SEND_VIDEO_NOTE ||
                this == SEND_MEDIA_GROUP ||
                this == SEND_LOCATION ||
                this == SEND_VENUE ||
                this == SEND_CONTACT ||
                this == SEND_POLL ||
                this == SEND_DICE ||
                this == SEND_STICKER ||
                this == SEND_GAME;
    }

    /**
     * 检查是否为编辑相关方法
     */
    public boolean isEditMethod() {
        return this == EDIT_MESSAGE_TEXT ||
                this == EDIT_MESSAGE_REPLY_MARKUP;
    }

    /**
     * 检查是否为管理员权限方法
     */
    public boolean requiresAdminPermission() {
        return this == KICK_CHAT_MEMBER ||
                this == UNBAN_CHAT_MEMBER ||
                this == RESTRICT_CHAT_MEMBER ||
                this == PROMOTE_CHAT_MEMBER ||
                this == SET_CHAT_TITLE ||
                this == SET_CHAT_DESCRIPTION ||
                this == SET_CHAT_PHOTO ||
                this == DELETE_CHAT_PHOTO ||
                this == PIN_CHAT_MESSAGE ||
                this == UNPIN_CHAT_MESSAGE ||
                this == UNPIN_ALL_CHAT_MESSAGES;
    }
}
