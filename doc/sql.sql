create database if not exists hyx_test;

use hyx_test;

-- auto-generated definition
create table fmk_dict
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    dict_type   varchar(100)                                 not null comment '字典类型，例如 gender, currency, country',
    dict_key    varchar(100)                                 not null comment '字典键，用于逻辑标识，如 MALE, USD',
    sort_order  int                         default 0        null comment '排序号',
    status      enum ('NORMAL', 'ABNORMAL') default 'NORMAL' null comment '状态：NORMAL 启用，ABNORMAL 禁用',
    remark      varchar(255)                                 null comment '备注说明',
    del_flag    enum ('NORMAL', 'ABNORMAL') default 'NORMAL' null comment '删除标志',
    create_user varchar(50)                                  not null comment '创建用户',
    create_date bigint                                       not null comment '创建时间（UTC毫秒）',
    update_user varchar(50)                                  null comment '更新用户',
    update_date bigint                                       not null comment '更新时间（UTC毫秒）',
    constraint uk_type_key
        unique (dict_type, dict_key)
)
    comment '系统字典主表';


-- auto-generated definition
create table fmk_dict_i18n
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    dict_type     varchar(100)                                 not null comment '字典类型，与主表一致',
    dict_key      varchar(100)                                 not null comment '字典键，与主表一致',
    language_code varchar(10)                                  not null comment '语言代码，例如 zh, en, ja',
    dict_value    varchar(255)                                 not null comment '显示名称，例如：男 / Male / 男性',
    remark        varchar(512)                                 null comment '描述信息，可选',
    del_flag      enum ('NORMAL', 'ABNORMAL') default 'NORMAL' null comment '删除标志',
    create_user   varchar(50)                                  not null comment '创建用户',
    create_date   bigint                                       not null comment '创建时间（UTC毫秒）',
    update_user   varchar(50)                                  null comment '更新用户',
    update_date   bigint                                       not null comment '更新时间（UTC毫秒）',
    constraint uk_dict_i18n
        unique (dict_type, dict_key, language_code)
)
    comment '系统字典多语言表';


-- auto-generated definition
create table fmk_multi_language_message
(
    id            bigint auto_increment comment 'Primary key ID; 主键ID'
        primary key,
    message_type  varchar(100)                                 not null comment 'Message type; 消息类型',
    message_key   varchar(100)                                 not null comment 'Message key; 消息键',
    language_code varchar(10)                                  not null comment 'Language code (en, zh); 语言代码',
    message_value varchar(512)                                 not null comment 'Message content; 消息内容',
    del_flag      enum ('NORMAL', 'ABNORMAL') default 'NORMAL' null comment 'Delete flag; 删除标志',
    create_user   varchar(50)                                  not null comment 'Creator user ID; 创建用户ID',
    create_date   bigint                                       not null comment 'Creation timestamp (UTC, milliseconds); 创建时间(UTC毫秒时间戳)',
    update_user   varchar(50)                                  null comment 'Updater user ID or name; 更新用户ID或名称',
    update_date   bigint                                       not null comment 'Update timestamp (UTC, milliseconds); 更新时间(UTC毫秒时间戳)',
    constraint uk_message_locale
        unique (message_type, message_key, language_code) comment 'Unique index on message and locale; 消息和语言唯一索引'
)
    comment 'System message content table; 系统消息内容表';





