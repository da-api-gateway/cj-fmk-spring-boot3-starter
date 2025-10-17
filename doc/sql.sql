create database if not exists xa_exchange;

use xa_exchange;

-- auto-generated definition
CREATE TABLE multi_language_message
(
    id            BIGINT AUTO_INCREMENT COMMENT 'Primary key ID; 主键ID'
        PRIMARY KEY,
    message_type  VARCHAR(100)                                 NOT NULL COMMENT 'Message type; 消息类型',
    message_key   VARCHAR(100)                                 NOT NULL COMMENT 'Message key; 消息键',
    language_code VARCHAR(10)                                  NOT NULL COMMENT 'Language code (en, zh); 语言代码',
    message_value VARCHAR(512)                                 NOT NULL COMMENT 'Message content; 消息内容',

    del_flag      ENUM ('NORMAL', 'ABNORMAL') DEFAULT 'NORMAL' NULL COMMENT 'Delete flag; 删除标志',

    create_user   VARCHAR(50)                                  NOT NULL COMMENT 'Creator user ID; 创建用户ID',
    create_date   BIGINT                                       NOT NULL COMMENT 'Creation timestamp (UTC, milliseconds); 创建时间(UTC毫秒时间戳)',

    update_user   VARCHAR(50)                                  NULL COMMENT 'Updater user ID or name; 更新用户ID或名称',
    update_date   BIGINT                                       NOT NULL COMMENT 'Update timestamp (UTC, milliseconds); 更新时间(UTC毫秒时间戳)',

    CONSTRAINT uk_message_locale
        UNIQUE (message_type, message_key, language_code) COMMENT 'Unique index on message and locale; 消息和语言唯一索引'
)
    COMMENT 'System message content table; 系统消息内容表';


-- ================== 初始化多语言异常信息 ==================
INSERT INTO multi_language_message (message_type, message_key, language_code, message_value, del_flag, create_user,
                                    create_date, update_user, update_date)
VALUES
-- ================= 系统异常 =================
('SYSTEM_ERROR', 'UNKNOWN_ERROR', 'en_US', 'Unknown system error', 'NORMAL', 0, 0, 0, 0),
('SYSTEM_ERROR', 'UNKNOWN_ERROR', 'zh_CN', '系统未知错误', 'NORMAL', 0, 0, 0, 0),

('SYSTEM_ERROR', 'SERVICE_UNAVAILABLE', 'en_US', 'Service temporarily unavailable', 'NORMAL', 0, 0, 0, 0),
('SYSTEM_ERROR', 'SERVICE_UNAVAILABLE', 'zh_CN', '服务暂时不可用', 'NORMAL', 0, 0, 0, 0),

('SYSTEM_ERROR', 'TIMEOUT', 'en_US', 'Request timeout', 'NORMAL', 0, 0, 0, 0),
('SYSTEM_ERROR', 'TIMEOUT', 'zh_CN', '请求超时', 'NORMAL', 0, 0, 0, 0),

-- ================= 业务异常 =================
('BUSINESS_ERROR', 'INVALID_PARAMETER', 'en_US', 'Invalid request parameter', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'INVALID_PARAMETER', 'zh_CN', '请求参数无效', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'ORDER_NOT_FOUND', 'en_US', 'Order not found', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'ORDER_NOT_FOUND', 'zh_CN', '订单未找到', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'INSUFFICIENT_BALANCE', 'en_US', 'Insufficient balance', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'INSUFFICIENT_BALANCE', 'zh_CN', '余额不足', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'DUPLICATE_OPERATION', 'en_US', 'Duplicate operation, please do not resubmit', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'DUPLICATE_OPERATION', 'zh_CN', '重复操作，请勿重复提交', 'NORMAL', 0, 0, 0, 0),

-- ================= 权限与认证异常 =================
('BUSINESS_ERROR', 'UNAUTHORIZED', 'en_US', 'Unauthorized, please log in', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'UNAUTHORIZED', 'zh_CN', '未授权，请登录', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'TOKEN_EXPIRED', 'en_US', 'Token expired, please log in again', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'TOKEN_EXPIRED', 'zh_CN', '登录凭证已过期，请重新登录', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'INVALID_CREDENTIALS', 'en_US', 'Invalid username or password', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'INVALID_CREDENTIALS', 'zh_CN', '用户名或密码错误', 'NORMAL', 0, 0, 0, 0),

-- ================= 网络与第三方异常 =================
('BUSINESS_ERROR', 'CONNECTION_FAILED', 'en_US', 'Connection failed', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'CONNECTION_FAILED', 'zh_CN', '连接失败', 'NORMAL', 0, 0, 0, 0),

('BUSINESS_ERROR', 'THIRD_PARTY_ERROR', 'en_US', 'Third-party service error', 'NORMAL', 0, 0, 0, 0),
('BUSINESS_ERROR', 'THIRD_PARTY_ERROR', 'zh_CN', '第三方服务异常', 'NORMAL', 0, 0, 0, 0);

























