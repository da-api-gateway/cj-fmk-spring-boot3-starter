// package com.cjlabs.db.requestinterceptor;
//
// import com.cjlabs.core.types.longs.FmkUserId;
// import com.cjlabs.core.types.strings.FmkToken;
// import com.cjlabs.core.types.strings.FmkTraceId;
// import com.cjlabs.db.token.IFmkTokenService;
// import com.cjlabs.domain.enums.ClientTypeEnum;
// import com.cjlabs.domain.enums.FmkLanguageEnum;
// import com.cjlabs.domain.enums.IEnumStr;
// import com.cjlabs.web.threadlocal.ClientInfo;
// import com.cjlabs.web.threadlocal.FmkContextInfo;
// import com.cjlabs.web.threadlocal.FmkContextUtil;
// import com.cjlabs.web.threadlocal.FmkUserInfo;
// import com.cjlabs.web.util.ClientInfoUtil;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.slf4j.MDC;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.util.Arrays;
// import java.util.Enumeration;
// import java.util.List;
// import java.util.Optional;
//
// import static com.cjlabs.domain.common.FmkConstant.*;
//
// /**
//  * 上下文拦截器
//  * 在请求开始时设置上下文信息，请求结束时清理
//  */
// @Slf4j
// @Component
// public class FmkContextInterceptor implements HandlerInterceptor {
//
//     @Autowired
//     private IFmkTokenService fmkTokenService;
//
//     /**
//      * 系统用户ID - 用于不需要登录的接口
//      */
//     private static final Long SYSTEM_USER_ID = 0L;
//
//     /**
//      * 需要设置系统用户的接口路径列表
//      */
//     private static final List<String> SYSTEM_USER_PATHS = Arrays.asList(
//             // FmkWebConstant.API_PREFIX + "/front/user/loginOrRegister"
//             // "/api/front/user/loginByCode",
//             // "/api/front/user/sendVerifyCode",
//             // "/api/front/user/forgetPassword",
//             // "/api/front/user/resetPassword",
//             // "/api/common/file/upload",
//             // "/api/front/city/list",
//             // "/api/front/district/list",
//             // "/api/front/house/list",
//             // "/api/front/project/list"
//     );
//
//     @Override
//     public boolean preHandle(HttpServletRequest request,
//                              HttpServletResponse response,
//                              Object handler) {
//         // 对于OPTIONS请求，直接放行
//         if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
//             return true;
//         }
//
//         try {
//             // 获取由TraceFilter创建的上下文信息
//             Optional<FmkContextInfo> contextInfoOpt = FmkContextUtil.getContextInfo();
//             if (contextInfoOpt.isEmpty()) {
//                 log.warn("FmkContextInterceptor|preHandle|上下文信息不存在，可能TraceFilter未正确执行|uri={}",
//                         request.getRequestURI());
//                 return true;
//             }
//
//             FmkContextInfo contextInfo = contextInfoOpt.get();
//
//             // 设置客户端信息
//             setClientInfo(request, contextInfo);
//
//             // 设置用户信息
//             setUserInfo(request, contextInfo);
//
//             // 设置请求信息
//             setRequestInfo(request, contextInfo);
//
//             // 设置请求头信息
//             setHeaders(request, contextInfo);
//
//             if (log.isDebugEnabled()) {
//                 log.debug("FmkContextInterceptor|preHandle|上下文信息增强完成|uri={}|userId={}|traceId={}",
//                         request.getRequestURI(),
//                         Optional.ofNullable(contextInfo.getUserId()).map(FmkUserId::getValue).orElse(null),
//                         Optional.ofNullable(contextInfo.getTraceId()).map(FmkTraceId::getValue).orElse(null));
//             }
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|preHandle|设置上下文信息失败|uri={}", request.getRequestURI(), e);
//         }
//
//         return true; // 即使失败也继续处理请求
//     }
//
//     @Override
//     public void afterCompletion(HttpServletRequest request,
//                                 HttpServletResponse response,
//                                 Object handler,
//                                 Exception ex) {
//         // 不需要清理上下文，由TraceFilter负责
//         // 只清理MDC中的用户ID，因为这是在拦截器中设置的
//         try {
//             MDC.remove(MDC_USER_ID);
//
//             if (log.isDebugEnabled()) {
//                 log.debug("FmkContextInterceptor|afterCompletion|清理MDC用户ID|uri={}", request.getRequestURI());
//             }
//         } catch (Exception e) {
//             log.warn("FmkContextInterceptor|afterCompletion|清理MDC用户ID失败|uri={}", request.getRequestURI(), e);
//         }
//     }
//
//     /**
//      * 设置用户信息
//      */
//     private void setUserInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
//         try {
//             String userToken = request.getHeader(HEADER_USER_TOKEN);
//             if (StringUtils.isBlank(userToken)) {
//                 if (log.isDebugEnabled()) {
//                     log.debug("FmkContextInterceptor|setUserInfo|未提供用户Token");
//                 }
//                 // 检查是否需要设置系统用户
//                 setSystemUserIfNeeded(request, contextInfo);
//                 return;
//             }
//
//             // 设置 token 到上下文
//             FmkToken fmkToken = FmkToken.ofNullable(userToken);
//             contextInfo.setToken(fmkToken);
//
//
//             Optional<FmkUserInfo> userInfoOptional = fmkTokenService.getUserIdByToken(fmkToken);
//             userInfoOptional.ifPresentOrElse(
//                     fmkUserInfo -> {
//                         // 设置用户信息和ID
//                         contextInfo.setUserInfo(fmkUserInfo);
//                         contextInfo.setUserId(fmkUserInfo.getUserId());
//
//                         // 添加用户ID到MDC，便于日志追踪
//                         MDC.put(MDC_USER_ID, String.valueOf(fmkUserInfo.getUserId().getValue()));
//
//                         // 获取并设置设备信息到Token服务中（用于活跃状态更新）
//                         updateTokenClientInfo(request, fmkToken);
//
//                         if (log.isDebugEnabled()) {
//                             log.debug("FmkContextInterceptor|setUserInfo|用户信息设置成功|userId={}",
//                                     fmkUserInfo.getUserId().getValue());
//                         }
//                     },
//                     () -> {
//                         log.warn("FmkContextInterceptor|setUserInfo|Token验证失败|token={}",
//                                 maskToken(userToken));
//                         // Token验证失败，检查是否需要设置系统用户
//                         setSystemUserIfNeeded(request, contextInfo);
//                     }
//             );
//
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|setUserInfo|设置用户信息失败", e);
//             // 异常情况下也检查是否需要设置系统用户
//             setSystemUserIfNeeded(request, contextInfo);
//         }
//     }
//
//     /**
//      * 掩码处理Token，只显示前几位
//      */
//     private String maskToken(String token) {
//         if (StringUtils.isBlank(token)) {
//             return "";
//         }
//         return token.substring(0, Math.min(token.length(), 10)) + "...";
//     }
//
//     /**
//      * 为特定接口设置系统用户信息
//      */
//     private void setSystemUserIfNeeded(HttpServletRequest request, FmkContextInfo contextInfo) {
//         try {
//             String requestUri = request.getRequestURI();
//
//             // 检查当前请求是否需要设置系统用户
//             boolean needSystemUser = SYSTEM_USER_PATHS.stream().anyMatch(requestUri::contains);
//
//             if (needSystemUser) {
//                 // 设置系统用户ID
//                 FmkUserId systemUserId = FmkUserId.ofNullable(SYSTEM_USER_ID);
//                 contextInfo.setUserInfoAndUserId(systemUserId);
//
//                 // 添加系统用户ID到MDC，便于日志追踪
//                 MDC.put(MDC_USER_ID, String.valueOf(SYSTEM_USER_ID));
//
//                 if (log.isDebugEnabled()) {
//                     log.debug("FmkContextInterceptor|setSystemUserIfNeeded|设置系统用户成功|uri={}|systemUserId={}",
//                             requestUri, SYSTEM_USER_ID);
//                 }
//             } else if (log.isDebugEnabled()) {
//                 log.debug("FmkContextInterceptor|setSystemUserIfNeeded|接口不需要系统用户|uri={}", requestUri);
//             }
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|setSystemUserIfNeeded|设置系统用户失败", e);
//         }
//     }
//
//     /**
//      * 设置客户端信息
//      */
//     private void setClientInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
//         try {
//             ClientInfo clientInfo = contextInfo.getClientInfo();
//
//             // 获取客户端IP
//             String clientIp = ClientInfoUtil.getClientIp(request);
//             clientInfo.setIpAddress(clientIp);
//
//             // 解析 User-Agent 获取设备信息
//             String userAgent = request.getHeader(HEADER_USER_AGENT);
//             if (StringUtils.isNotBlank(userAgent)) {
//                 ClientInfoUtil.parseUserAgent(userAgent, clientInfo);
//                 clientInfo.setUserAgent(userAgent);
//             } else {
//                 setDefaultClientInfo(clientInfo);
//             }
//
//             // 设置自定义请求头信息
//             setCustomHeaders(request, clientInfo);
//
//             if (log.isDebugEnabled()) {
//                 log.debug("FmkContextInterceptor|setClientInfo|客户端信息设置成功|ip={}|os={}|browser={}",
//                         clientIp, clientInfo.getOperatingSystem(), clientInfo.getBrowser());
//             }
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|setClientInfo|设置客户端信息失败", e);
//             // 设置默认值防止后续处理出错
//             setDefaultClientInfo(contextInfo.getClientInfo());
//         }
//     }
//
//     /**
//      * 设置默认客户端信息（异常情况下使用）
//      */
//     private void setDefaultClientInfo(ClientInfo clientInfo) {
//         clientInfo.setIpAddress("unknown");
//         // clientInfo.setDeviceType(DeviceTypeEnum.WEB);
//         clientInfo.setOperatingSystem("Unknown");
//         clientInfo.setBrowser("Unknown");
//         clientInfo.setUserAgent("Unknown");
//     }
//
//     /**
//      * 设置自定义请求头信息
//      */
//     private void setCustomHeaders(HttpServletRequest request, ClientInfo clientInfo) {
//         try {
//             // 设备版本
//             String deviceVersion = request.getHeader(HEADER_CLIENT_TYPE);
//             clientInfo.setDeviceVersion(StringUtils.isNotBlank(deviceVersion) ? deviceVersion : "unknown");
//
//             // 引用页
//             String referer = request.getHeader(HEADER_REFERER);
//             if (StringUtils.isNotBlank(referer)) {
//                 clientInfo.setReferrer(referer);
//             }
//
//             // 设备类型（如果请求头中有，则覆盖User-Agent解析的结果）
//             String headerClientType = request.getHeader(HEADER_CLIENT_TYPE);
//             if (StringUtils.isNotBlank(headerClientType)) {
//                 ClientTypeEnum clientTypeEnum = ClientInfoUtil.parseClientTypeFromString(headerClientType);
//                 clientInfo.setClientType(clientTypeEnum);
//
//                 if (log.isDebugEnabled()) {
//                     log.debug("FmkContextInterceptor|setCustomHeaders|使用请求头设备类型|headerType={}|parsedType={}",
//                             headerClientType, clientTypeEnum);
//                 }
//             }
//         } catch (Exception e) {
//             log.warn("FmkContextInterceptor|setCustomHeaders|设置自定义请求头失败", e);
//         }
//     }
//
//     /**
//      * 设置请求信息
//      */
//     private void setRequestInfo(HttpServletRequest request, FmkContextInfo contextInfo) {
//         try {
//             // 设置请求URI（如果TraceFilter已经设置，则不需要再设置）
//             if (contextInfo.getRequestUri() == null) {
//                 String requestUri = request.getRequestURI();
//                 contextInfo.setRequestUri(requestUri);
//             }
//
//             // 设置语言
//             String deviceLanguage = request.getHeader(HEADER_DEVICE_LANGUAGE);
//             if (StringUtils.isNotBlank(deviceLanguage)) {
//                 IEnumStr.getEnumByCode(deviceLanguage, FmkLanguageEnum.class)
//                         .ifPresent(language -> {
//                             contextInfo.setLanguage(language);
//                             if (log.isDebugEnabled()) {
//                                 log.debug("FmkContextInterceptor|setRequestInfo|语言设置成功|language={}", deviceLanguage);
//                             }
//                         });
//             }
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|setRequestInfo|设置请求信息失败", e);
//         }
//     }
//
//     /**
//      * 设置所有请求头
//      */
//     private void setHeaders(HttpServletRequest request, FmkContextInfo contextInfo) {
//         try {
//             Enumeration<String> headerNames = request.getHeaderNames();
//             int headerCount = 0;
//
//             while (headerNames.hasMoreElements()) {
//                 String headerName = headerNames.nextElement();
//                 String headerValue = request.getHeader(headerName);
//
//                 if (StringUtils.isNotBlank(headerName) && headerValue != null) {
//                     contextInfo.getHeaders().put(headerName, headerValue);
//                     headerCount++;
//                 }
//             }
//
//             if (log.isDebugEnabled()) {
//                 log.debug("FmkContextInterceptor|setHeaders|请求头设置完成|count={}", headerCount);
//             }
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|setHeaders|设置请求头失败", e);
//         }
//     }
//
//     /**
//      * 更新Token的设备信息（用于活跃状态追踪）
//      */
//     private void updateTokenClientInfo(HttpServletRequest request, FmkToken fmkToken) {
//         try {
//             if (fmkToken == null || StringUtils.isBlank(fmkToken.getValue())) {
//                 return;
//             }
//
//             // 获取设备信息
//             fmkTokenService.get(fmkToken).ifPresent(deviceInfo -> {
//                 // 更新最后活跃时间和IP（如果需要）
//                 String currentIp = ClientInfoUtil.getClientIp(request);
//                 String storedIp = deviceInfo.get();
//
//                 if (!currentIp.equals(storedIp)) {
//                     log.info("FmkContextInterceptor|updateTokenDeviceInfo|检测到IP变化|oldIp={}|newIp={}",
//                             storedIp, currentIp);
//                     // 这里可以添加更新设备信息的逻辑
//                 }
//             });
//         } catch (Exception e) {
//             log.error("FmkContextInterceptor|updateTokenDeviceInfo|更新设备信息失败", e);
//         }
//     }
// }