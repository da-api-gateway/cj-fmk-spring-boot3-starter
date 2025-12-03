//package com.cjlabs.db.token.mapper;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.cjlabs.core.time.FmkInstantUtil;
//import com.cjlabs.core.types.longs.FmkUserId;
//import com.cjlabs.core.types.strings.FmkToken;
//import com.cjlabs.db.domain.FmkOrderItem;
//import com.cjlabs.db.domain.FmkPageResponse;
//import com.cjlabs.db.domain.FmkRequest;
//import com.cjlabs.db.mp.FmkService;
//import com.cjlabs.db.token.enums.TokenStatusEnum;
//import com.cjlabs.db.token.mysql.LoginInfoToken;
//import com.cjlabs.domain.enums.NormalEnum;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.cjlabs.web.check.FmkCheckUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
///**
// * sys_user_info_token 管理员访问令牌表
// * 封装所有Token相关的数据库操作
// */
//@Slf4j
//@Service
//public class LoginInfoTokenWrapMapper extends FmkService<LoginInfoTokenMapper, LoginInfoToken> {
//
//    protected LoginInfoTokenWrapMapper(LoginInfoTokenMapper mapper) {
//        super(mapper);
//    }
//
//    /**
//     * 根据Token查询有效的Token信息
//     */
//    public Optional<LoginInfoToken> getTokenDbByToken(FmkToken token) {
//        if (token == null) {
//            return Optional.empty();
//        }
//
//        try {
//            LambdaQueryWrapper<LoginInfoToken> wrapper = buildLambdaQuery();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .eq(LoginInfoToken::getDelFlag, NormalEnum.NORMAL)
//                    .gt(LoginInfoToken::getExpireTime, FmkInstantUtil.now())
//                    .last("LIMIT 1");
//
//            LoginInfoToken entity = this.getByCondition(wrapper);
//            return Optional.ofNullable(entity);
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|getTokenDbByToken|查询失败", e);
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * 根据Token查询用户ID（仅查询有效Token）
//     */
//    public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
//        if (token == null) {
//            return Optional.empty();
//        }
//
//        try {
//            LambdaQueryWrapper<LoginInfoToken> wrapper = buildLambdaQuery();
//            wrapper.select(LoginInfoToken::getUserId)
//                    .eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .eq(LoginInfoToken::getDelFlag, NormalEnum.NORMAL)
//                    .gt(LoginInfoToken::getExpireTime, FmkInstantUtil.now())
//                    .last("LIMIT 1");
//
//            LoginInfoToken entity = this.getByCondition(wrapper);
//            return Optional.ofNullable(entity).map(LoginInfoToken::getUserId);
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|getUserIdByToken|查询失败", e);
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * 根据Token查询Token信息（包含已过期的）
//     */
//    public Optional<LoginInfoToken> getTokenInfoByToken(FmkToken token) {
//        if (token == null) {
//            return Optional.empty();
//        }
//
//        try {
//            LambdaQueryWrapper<LoginInfoToken> wrapper = buildLambdaQuery();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getDelFlag, NormalEnum.NORMAL)
//                    .last("LIMIT 1");
//
//            LoginInfoToken entity = this.getByCondition(wrapper);
//            return Optional.ofNullable(entity);
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|getAllTokenInfoByToken|查询失败", e);
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * 验证Token是否有效
//     */
//    public boolean checkTokenValid(FmkToken token) {
//        if (token == null) {
//            return false;
//        }
//
//        try {
//            LambdaQueryWrapper<LoginInfoToken> wrapper = buildLambdaQuery();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .eq(LoginInfoToken::getDelFlag, NormalEnum.NORMAL)
//                    .gt(LoginInfoToken::getExpireTime, FmkInstantUtil.now());
//
//            Long count = this.countByCondition(wrapper);
//            return count != null && count > 0;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|checkTokenValid|验证失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * 刷新Token过期时间
//     */
//    public boolean refreshTokenExpireTime(FmkToken token) {
//        if (token == null) {
//            log.info("LoginInfoTokenWrapMapper|refreshTokenExpireTime|token is null");
//            return false;
//        }
//
//        try {
//            Optional<LoginInfoToken> tokenOptional = getTokenDbByToken(token);
//            if (tokenOptional.isEmpty()) {
//                log.info("LoginInfoTokenWrapMapper|refreshTokenExpireTime|tokenOptional is null");
//                return false;
//            }
//            LoginInfoToken loginInfoToken = tokenOptional.get();
//
//            Instant expireTime = loginInfoToken.getExpireTime();
//            Instant newExpireTime = expireTime.plus(12, ChronoUnit.HOURS);
//            loginInfoToken.setExpireTime(newExpireTime);
//
//            LambdaUpdateWrapper<LoginInfoToken> wrapper = buildLambdaUpdate();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .eq(LoginInfoToken::getDelFlag, NormalEnum.NORMAL)
//                    .set(LoginInfoToken::getExpireTime, newExpireTime);
//
//            // 👉 修改：传入实体对象和wrapper
//            int updated = this.updateByCondition(loginInfoToken, wrapper);
//            log.info("LoginInfoTokenWrapMapper|refreshTokenExpireTime|刷新Token过期时间|success={}", updated > 0);
//            return updated > 0;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|refreshTokenExpireTime|刷新失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * 撤销指定Token
//     */
//    public boolean revokeTokenByToken(FmkToken token) {
//        if (token == null) {
//            return false;
//        }
//
//        try {
//            LambdaUpdateWrapper<LoginInfoToken> wrapper = buildLambdaUpdate();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .set(LoginInfoToken::getStatus, TokenStatusEnum.REVOKED);
//
//            // 👉 修改：传入 null 作为实体（只使用 wrapper 中的 set 条件）
//            int updated = this.updateByCondition(null, wrapper);
//            log.info("LoginInfoTokenWrapMapper|revokeTokenByToken|撤销Token|success={}", updated > 0);
//            return updated > 0;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|revokeTokenByToken|撤销失败", e);
//            return false;
//        }
//    }
//
//
//    /**
//     * 撤销用户的所有有效Token
//     */
//    public int revokeAllTokensByUserId(FmkUserId userId) {
//        if (userId == null) {
//            return 0;
//        }
//
//        try {
//            LambdaUpdateWrapper<LoginInfoToken> wrapper = buildLambdaUpdate();
//            wrapper.eq(LoginInfoToken::getUserId, userId)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .set(LoginInfoToken::getStatus, TokenStatusEnum.REVOKED);
//
//            // 👉 修改：传入 null 作为实体
//            int updated = this.updateByCondition(null, wrapper);
//            log.info("LoginInfoTokenWrapMapper|revokeAllTokensByUserId|撤销用户所有Token|userId={}|count={}",
//                    userId.getValue(), updated);
//            return updated;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|revokeAllTokensByUserId|撤销失败|userId={}", userId.getValue(), e);
//            return 0;
//        }
//    }
//
//    /**
//     * 清理过期的Token（将状态改为EXPIRED）
//     */
//    public int cleanExpiredTokens() {
//        try {
//            Instant now = FmkInstantUtil.now();
//
//            LambdaUpdateWrapper<LoginInfoToken> wrapper = buildLambdaUpdate();
//            wrapper.eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .lt(LoginInfoToken::getExpireTime, now)
//                    .set(LoginInfoToken::getStatus, TokenStatusEnum.EXPIRED);
//
//            // 👉 修改：传入 null 作为实体
//            int updated = this.updateByCondition(null, wrapper);
//
//            if (updated > 0) {
//                log.info("LoginInfoTokenWrapMapper|cleanExpiredTokens|清理过期Token|count={}", updated);
//            }
//            return updated;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|cleanExpiredTokens|清理失败", e);
//            return 0;
//        }
//    }
//
//    /**
//     * 更新Token的最后活跃时间（使用基类的update_date字段）
//     */
//    public boolean updateLastActiveTime(FmkToken token) {
//        if (token == null) {
//            return false;
//        }
//
//        try {
//            Instant now = FmkInstantUtil.now();
//
//            LambdaUpdateWrapper<LoginInfoToken> wrapper = buildLambdaUpdate();
//            wrapper.eq(LoginInfoToken::getToken, token)
//                    .eq(LoginInfoToken::getStatus, TokenStatusEnum.ACTIVE)
//                    .set(LoginInfoToken::getUpdateDate, now);  // 👈 使用 UpdateDate
//
//            // 👉 修改：传入 null 作为实体
//            int updated = this.updateByCondition(null, wrapper);
//            return updated > 0;
//        } catch (Exception e) {
//            log.error("LoginInfoTokenWrapMapper|updateLastActiveTime|更新失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * Token 掩码处理
//     */
//    private String maskToken(String token) {
//        if (token == null || token.length() <= 10) {
//            return "***";
//        }
//        return token.substring(0, 10) + "...";
//    }
//
//    /**
//     * 分页查询
//     */
//    public FmkPageResponse<LoginInfoToken> pageQuery(FmkRequest<LoginInfoToken> input) {
//        // 参数校验
//        FmkCheckUtil.checkInput(Objects.isNull(input));
//        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));
//
//        // 构建分页对象
//        Page<LoginInfoToken> page = new Page<>(input.getCurrent(), input.getSize());
//        LoginInfoToken request = input.getRequest();
//
//        // 构建查询条件
//        LambdaQueryWrapper<LoginInfoToken> lambdaQuery = buildLambdaQuery();
//        List<FmkOrderItem> orderItemList = input.getOrderItemList();
//
//        // 执行分页查询
//        IPage<LoginInfoToken> dbPage = super.pageByCondition(page, lambdaQuery, orderItemList);
//
//        return FmkPageResponse.of(dbPage);
//    }
//}