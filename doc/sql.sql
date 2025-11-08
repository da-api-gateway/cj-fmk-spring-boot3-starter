create database if not exists hyx_test;

use hyx_test;

#
# account_type ENUM(
#     'WALLET',      -- 钱包账户（充值提现入口）
#     'SPOT',        -- 现货账户（普通买卖）
#     'MARGIN',      -- 杠杆账户（借币交易）
#     'CONTRACT',    -- 合约账户（衍生品保证金）
#     'EARN',        -- 理财账户（锁仓生息）
#     'STAKING',     -- 质押账户（节点/链上质押）
#     'BONUS',       -- 赠币账户（游戏或活动赠送）
#     'FROZEN'       -- 冻结账户（风控冻结或系统冻结）
# ) NOT NULL DEFAULT 'SPOT' COMMENT '账户类型';


CREATE TABLE `user_account_asset`
(
    `id`                 BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`            BIGINT          NOT NULL COMMENT '用户ID',
    `account_type`       VARCHAR(30)     NOT NULL COMMENT '账户类型：WALLET,SPOT,CONTRACT,EARN,STAKING,BONUS,FROZEN',

    -- 币种信息
    `chain_type`         VARCHAR(20)     NOT NULL COMMENT '链类型：ETH,BSC,SOL,TRON等',
    `coin_symbol`        VARCHAR(20)     NOT NULL COMMENT '币种符号：USDT,ETH,BTC等',
    `token_contract`     VARCHAR(100)             DEFAULT NULL COMMENT '代币合约地址（主链币为NULL）',
    `decimals`           INT             NOT NULL DEFAULT 18 COMMENT '币种精度',

    -- 余额信息（使用 DECIMAL 存储，避免精度丢失）
    `available_balance`  DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '可用余额',
    `frozen_balance`     DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '冻结余额（订单冻结、提现冻结等）',
    `locked_balance`     DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '锁定余额（质押锁定、理财锁定等）',
    `total_balance`      DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '总余额 = 可用 + 冻结 + 锁定',

    -- 统计信息
    `total_deposit`      DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '累计充值',
    `total_withdraw`     DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '累计提现',
    `total_transfer_in`  DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '累计转入',
    `total_transfer_out` DECIMAL(36, 18) NOT NULL DEFAULT 0 COMMENT '累计转出',
    `last_update_time`   DATETIME                 DEFAULT NULL COMMENT '最后更新时间',

    -- 基础字段
    `del_flag`           TINYINT         NOT NULL DEFAULT 0,
    `create_user`        BIGINT,
    `create_date`        DATETIME                 DEFAULT CURRENT_TIMESTAMP,
    `update_user`        BIGINT,
    `update_date`        DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_account_coin` (`user_id`, `account_type`, `chain_type`, `coin_symbol`, `token_contract`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_type` (`account_type`),
    KEY `idx_chain_coin` (`chain_type`, `coin_symbol`),
    KEY `idx_user_account` (`user_id`, `account_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户多账户资产表';











