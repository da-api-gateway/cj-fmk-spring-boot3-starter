package com.cjlabs.web.requestinterceptor;

import com.xodo.business.common.user.enums.DeviceTypeEnum;
import com.xodo.fmk.common.LanguageEnum;
import com.xodo.fmk.core.ClientInfo;
import com.xodo.fmk.core.FmkContextInfo;
import com.xodo.fmk.core.FmkUserInfo;
import com.xodo.fmk.core.enums.IEnumStr;
import com.xodo.fmk.jdk.basetype.type.FmkToken;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import com.xodo.fmk.jdk.basetype.type.FmkUserId;
import com.xodo.fmk.web.FmkContextUtil;
import com.xodo.fmk.web.token.DeviceInfo;
import com.xodo.fmk.web.token.FmkTokenService;
import com.xodo.fmk.web.trace.FmkTraceService;
import com.xodo.fmk.web.util.ClientInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import static com.xodo.fmk.common.FmkConstant.*;


/**
 * 上下文拦截器
 * 在请求开始时设置上下文信息，请求结束时清理
 */
@Slf4j
@Component
public class FmkContextInterceptor implements HandlerInterceptor {

    @Autowired
    private FmkTokenService fmkTokenService;

    @Autowired
    private FmkTraceService fmkTraceService;

    /**
     * 系统用户ID - 用于不需要登录的接口
     * <p>
     * {@link SYSTEM_USER_PATHS}
     */
    private static final Long SYSTEM_USER_ID = 0L;

    /**
     * 需要设置系统用户的接口路径列表
     * <p>
     * {@link SYSTEM_USER_ID}
     */
    private static final List<String> SYSTEM_USER_PATHS = Arrays.asList(
            API_PREFIX + "/front/user/loginOrRegister"
            // "/api/front/user/loginByCode",
            // "/api/front/user/sendVerifyCode",
            // "/api/front/user/forgetPassword",
            // "/api/front/user/resetPassword",
            // "/api/common/file/upload",
            // "/api/front/city/list",
            // "/api/front/district/list",
            // "/api/front/house/list",
            // "/api/front/project/list"
    );

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        try {
            // 🔥 对于OPTIONS请求，直接放行，不做任何处理
            if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
                return true;
            }

            // 创建上下文信息
            FmkContextInfo contextInfo = new FmkContextInfo();

            // 设置 TraceId
            setTraceId(request, response, contextInfo);

            // 设置客户端信息
            setClientInfo(request, contextInfo);

            // 设置用户信息
            setUserInfo(request, contextInfo);

            // 设置请求信息
            setRequestInfo(request, contextInfo);

            // 设置请求头信息
            setHeaders(request, contextInfo);

            // 设置上下文
            FmkContextUtil.setContextInfo(contextInfo);

            log.info("FmkContextInterceptor|preHandle|上下文设置完成|uri={}|userId={}|traceId={}|deviceType={}",
                    request.getRequestURI(),
                    contextInfo.getUserId() != null ? contextInfo.getUserId().getValue() : null,
                    contextInfo.getTraceId() != null ? contextInfo.getTraceId().getValue() : null,
                    contextInfo.getClientInfo().getDeviceType());

            return true;
        } catch (Exception e) {
            log.error("FmkContextInterceptor|preHandle|设置上下文信息失败|uri={}", request.getRequestURI(), e);
            return true; // 即使失败也继续处理请求
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        try {
            // 清理MDC上下文
            clearMDCContext();

            // 清理Framework上下文
            FmkContextUtil.clear();

            log.info("FmkContextInterceptor|afterCompletion|清理请求上下文完成|uri={}", request.getRequestURI());
        } catch (Exception e) {
            log.error("FmkContextInterceptor|afterCompletion|清理上下文信息失败|uri={}", request.getRequestURI(), e);
        }
    }

    /**
     * 设置 TraceId
     */
    private void setTraceId(HttpServletRequest request,
                            HttpServletResponse response,
                            FmkContextInfo contextInfo) {
        try {
            FmkTraceId fmkTraceId = fmkTraceService.getOrGenerateTraceId(request);
            contextInfo.setTraceId(fmkTraceId);

            // 设置到MDC，用于日志输出
            if (fmkTraceId != null && StringUtils.isNotBlank(fmkTraceId.getValue())) {
                MDC.put(MDC_TRACE_ID, fmkTraceId.getValue());
                response.setHeader(HEADER_TRACE_ID, fmkTraceId.getValue());
                log.info("FmkContextInterceptor|setTraceId|TraceId设置成功|traceId={}", fmkTraceId.getValue());
            } else {
                log.warn("FmkContextInterceptor|setTraceId|生成的TraceId为空");
            }
        } catch (Exception e) {
            log.error("FmkContextInterceptor|setTraceId|TraceId设置失败", e);
        }
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
        try {
            String userToken = request.getHeader(HEADER_USER_TOKEN);
            if (StringUtils.isBlank(userToken)) {
                log.info("FmkContextInterceptor|setUserInfo|未提供用户Token");
                // 检查是否需要设置系统用户
                setSystemUserIfNeeded(request, contextInfo);
                return;
            }

            // 设置 token 到上下文
            // 通过 FmkTokenService 获取用户信息
            FmkToken fmkToken = FmkToken.ofNullable(userToken);
            contextInfo.setToken(fmkToken);

            Optional<FmkUserInfo> fmkUserInfoOp = fmkTokenService.getUserByToken(fmkToken);

            if (fmkUserInfoOp.isPresent()) {
                FmkUserInfo fmkUserInfo = fmkUserInfoOp.get();
                contextInfo.setUserInfo(fmkUserInfo);

                // 设置用户ID
                contextInfo.setUserId(fmkUserInfo.getUserId());

                // 添加用户ID到MDC，便于日志追踪
                MDC.put(MDC_USER_ID, String.valueOf(fmkUserInfo.getUserId()));

                // 获取并设置设备信息到Token服务中（用于活跃状态更新）
                updateTokenDeviceInfo(request, fmkToken);

                log.info("FmkContextInterceptor|setUserInfo|用户信息设置成功|userId={}", fmkUserInfo.getUserId());
            } else {
                log.warn("FmkContextInterceptor|setUserInfo|Token验证失败|token={}",
                        userToken.substring(0, Math.min(userToken.length(), 10)) + "...");
                // Token验证失败，检查是否需要设置系统用户
                setSystemUserIfNeeded(request, contextInfo);
            }

        } catch (Exception e) {
            log.error("FmkContextInterceptor|setUserInfo|设置用户信息失败", e);
            // 异常情况下也检查是否需要设置系统用户
            setSystemUserIfNeeded(request, contextInfo);
        }
    }

    /**
     * 为特定接口设置系统用户信息
     */
    private void setSystemUserIfNeeded(HttpServletRequest request, FmkContextInfo contextInfo) {
        try {
            String requestUri = request.getRequestURI();

            // 检查当前请求是否需要设置系统用户
            boolean needSystemUser = SYSTEM_USER_PATHS.stream().anyMatch(requestUri::contains);

            if (needSystemUser) {
                // 设置系统用户ID
                FmkUserId systemUserId = FmkUserId.ofNullable(SYSTEM_USER_ID);
                FmkUserInfo systemUserInfo = new FmkUserInfo();
                systemUserInfo.setUserId(systemUserId);
                contextInfo.setUserInfo(systemUserInfo);
                contextInfo.setUserId(systemUserId);

                // 添加系统用户ID到MDC，便于日志追踪
                MDC.put(MDC_USER_ID, String.valueOf(SYSTEM_USER_ID));

                log.info("FmkContextInterceptor|setSystemUserIfNeeded|设置系统用户成功|uri={}|systemUserId={}",
                        requestUri, SYSTEM_USER_ID);
            } else {
                log.info("FmkContextInterceptor|setSystemUserIfNeeded|接口不需要系统用户|uri={}", requestUri);
            }
        } catch (Exception e) {
            log.error("FmkContextInterceptor|setSystemUserIfNeeded|设置系统用户失败", e);
        }
    }

    /**
     * 设置客户端信息
     */
    private void setClientInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
        try {
            ClientInfo clientInfo = contextInfo.getClientInfo();

            // 使用优化后的 ClientInfoUtil 来解析客户端信息
            String clientIp = ClientInfoUtil.getClientIp(request);
            clientInfo.setIpAddress(clientIp);

            // 解析 User-Agent 获取设备信息
            String userAgent = request.getHeader(HEADER_USER_AGENT);
            if (StringUtils.isNotBlank(userAgent)) {
                ClientInfoUtil.parseUserAgent(userAgent, clientInfo);
            } else {
                log.info("FmkContextInterceptor|setClientInfo|User-Agent为空，使用默认设备信息");
                clientInfo.setDeviceType(DeviceTypeEnum.WEB);
                clientInfo.setOperatingSystem("Unknown");
                clientInfo.setBrowser("Unknown");
            }

            // 设置自定义请求头信息
            setCustomHeaders(request, clientInfo);

            log.info("FmkContextInterceptor|setClientInfo|客户端信息设置成功|ip={}|deviceType={}|os={}|browser={}",
                    clientIp, clientInfo.getDeviceType(), clientInfo.getOperatingSystem(), clientInfo.getBrowser());

        } catch (Exception e) {
            log.error("FmkContextInterceptor|setClientInfo|设置客户端信息失败", e);
            // 设置默认值防止后续处理出错
            setDefaultClientInfo(contextInfo.getClientInfo());
        }
    }

    /**
     * 设置默认客户端信息（异常情况下使用）
     */
    private void setDefaultClientInfo(ClientInfo clientInfo) {
        clientInfo.setIpAddress("unknown");
        clientInfo.setDeviceType(DeviceTypeEnum.WEB);
        clientInfo.setOperatingSystem("Unknown");
        clientInfo.setBrowser("Unknown");
        clientInfo.setUserAgent("Unknown");
    }

    /**
     * 设置自定义请求头信息
     */
    private void setCustomHeaders(HttpServletRequest request, ClientInfo clientInfo) {
        try {
            // 设备相关
            String deviceVersion = request.getHeader(HEADER_DEVICE_VERSION);
            if (StringUtils.isNotBlank(deviceVersion)) {
                clientInfo.setDeviceVersion(deviceVersion);
            } else {
                clientInfo.setDeviceVersion("unknown");
            }

            // 标准HTTP头
            String referer = request.getHeader(HEADER_REFERER);
            if (StringUtils.isNotBlank(referer)) {
                clientInfo.setReferrer(referer);
            }

            // 如果请求头中有设备类型，优先使用请求头的值（覆盖User-Agent解析的结果）
            String headerDeviceType = request.getHeader(HEADER_DEVICE_TYPE);
            if (StringUtils.isNotBlank(headerDeviceType)) {
                DeviceTypeEnum deviceType = ClientInfoUtil.parseDeviceTypeFromString(headerDeviceType);
                clientInfo.setDeviceType(deviceType);
                log.info("FmkContextInterceptor|setCustomHeaders|使用请求头设备类型|headerType={}|parsedType={}",
                        headerDeviceType, deviceType);
            }

        } catch (Exception e) {
            log.warn("FmkContextInterceptor|setCustomHeaders|设置自定义请求头失败", e);
        }
    }

    /**
     * 设置请求信息
     */
    private void setRequestInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
        try {
            // 设置请求URI
            String requestUri = request.getRequestURI();
            contextInfo.setRequestUri(requestUri);

            // 设置语言
            String deviceLanguage = request.getHeader(HEADER_DEVICE_LANGUAGE);
            if (StringUtils.isNotBlank(deviceLanguage)) {
                Optional<LanguageEnum> enumOptional = IEnumStr.getEnumByCode(deviceLanguage, LanguageEnum.class);
                if (enumOptional.isPresent()) {
                    contextInfo.setLanguage(enumOptional.get());
                    log.info("FmkContextInterceptor|setRequestInfo|语言设置成功|language={}", deviceLanguage);
                } else {
                    log.warn("FmkContextInterceptor|setRequestInfo|不支持的语言代码|language={}", deviceLanguage);
                }
            }

            log.info("FmkContextInterceptor|setRequestInfo|请求信息设置成功|uri={}|language={}",
                    requestUri, deviceLanguage);

        } catch (Exception e) {
            log.error("FmkContextInterceptor|setRequestInfo|设置请求信息失败", e);
        }
    }

    /**
     * 设置所有请求头
     */
    private void setHeaders(HttpServletRequest request, FmkContextInfo contextInfo) {
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            int headerCount = 0;

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);

                if (StringUtils.isNotBlank(headerName) && headerValue != null) {
                    contextInfo.getHeaders().put(headerName, headerValue);
                    headerCount++;
                }
            }

            log.info("FmkContextInterceptor|setHeaders|请求头设置完成|count={}", headerCount);

        } catch (Exception e) {
            log.error("FmkContextInterceptor|setHeaders|设置请求头失败", e);
        }
    }

    /**
     * 更新Token的设备信息（用于活跃状态追踪）
     */
    private void updateTokenDeviceInfo(HttpServletRequest request, FmkToken fmkToken) {
        try {
            if (fmkToken == null || StringUtils.isBlank(fmkToken.getValue())) {
                return;
            }

            // 获取设备信息
            Optional<DeviceInfo> deviceInfoOp = fmkTokenService.getDeviceInfoByToken(fmkToken);
            if (deviceInfoOp.isPresent()) {
                DeviceInfo deviceInfo = deviceInfoOp.get();

                // 更新最后活跃时间和IP（如果需要）
                String currentIp = ClientInfoUtil.getClientIp(request);
                String storedIp = deviceInfo.getIpAddress();

                if (!currentIp.equals(storedIp)) {
                    log.info("FmkContextInterceptor|updateTokenDeviceInfo|检测到IP变化|oldIp={}|newIp={}",
                            storedIp, currentIp);
                    // 这里可以添加更新设备信息的逻辑
                }

                log.info("FmkContextInterceptor|updateTokenDeviceInfo|设备信息检查完成|currentIp={}", currentIp);
            } else {
                log.info("FmkContextInterceptor|updateTokenDeviceInfo|未找到设备信息");
            }
        } catch (Exception e) {
            log.error("FmkContextInterceptor|updateTokenDeviceInfo|更新设备信息失败", e);
        }
    }

    /**
     * 清理MDC上下文
     */
    private void clearMDCContext() {
        try {
            MDC.remove(MDC_TRACE_ID);
            MDC.remove(MDC_USER_ID);
            log.info("FmkContextInterceptor|clearMDCContext|MDC清理完成");
        } catch (Exception e) {
            log.warn("FmkContextInterceptor|clearMDCContext|清理MDC上下文失败", e);
        }
    }
}