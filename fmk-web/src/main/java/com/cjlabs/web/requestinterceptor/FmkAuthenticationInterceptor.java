// package com.cjlabs.web.requestinterceptor;
//
// import com.xodo.business.common.dict.bo.DictMaintenanceNoticeBO;
// import com.xodo.business.common.dict.service.CommonDictInfoServiceV2;
// import com.xodo.fmk.core.FmkResult;
// import com.xodo.fmk.jdk.basetype.type.FmkUserId;
// import com.xodo.fmk.json.FmkJacksonUtil;
// import com.xodo.fmk.web.FmkContextUtil;
// import com.xodo.fmk.web.annotation.AdminAuth;
// import com.xodo.fmk.web.annotation.NoAuth;
// import com.xodo.fmk.web.exception.ExceptionDbInterface;
// import com.xodo.fmk.web.exception.ExceptionService;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Component;
// import org.springframework.util.AntPathMatcher;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.method.HandlerMethod;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.util.Optional;
// import java.util.Set;
//
// import static com.xodo.fmk.common.FmkConstant.API_PREFIX;
//
// /**
//  * 登录认证拦截器
//  * 验证用户是否已登录，对于需要登录的接口进行拦截
//  */
// @Slf4j
// @Component
// public class FmkAuthenticationInterceptor implements HandlerInterceptor {
//     @Autowired
//     private FmkAdminUserCheckService fmkAdminUserCheckService;
//     @Autowired
//     private ExceptionService exceptionService;
//     @Autowired
//     private CommonDictInfoServiceV2 commonDictInfoServiceV2;
//
//     private final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//     /**
//      * 不需要登录的接口路径列表（白名单）
//      */
//     private static final Set<String> NO_AUTH_REQUIRED_PATHS = Set.of(
//             // API_PREFIX + "/front/user/register",
//             // API_PREFIX + "/front/user/tg/register",
//             API_PREFIX + "/front/user/loginOrRegister",
//             API_PREFIX + "/admin/sysAdminUser/login",
//
//             API_PREFIX + "/common/**",
//
//             "/favicon.ico"
//     );
//
//     /**
//      * 系统管理接口（需要特殊权限）
//      */
//     private static final Set<String> ADMIN_REQUIRED_PATHS = Set.of(
//             API_PREFIX + "/admin/**"
//     );
//
//     @Override
//     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//         try {
//             // 对于OPTIONS请求，直接放行
//             if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
//                 return true;
//             }
//
//             Optional<DictMaintenanceNoticeBO> systemMaintenanceConfig = commonDictInfoServiceV2.getSystemMaintenanceConfig();
//             if (systemMaintenanceConfig.isPresent()) {
//                 DictMaintenanceNoticeBO dictMaintenanceNoticeBO = systemMaintenanceConfig.get();
//                 if (dictMaintenanceNoticeBO.isEnabledFlag()) {
//                     sendErrorResponse(response, 500,
//                             dictMaintenanceNoticeBO.getZhMsg(), dictMaintenanceNoticeBO.getEnMsg(),
//                             null, null);
//                     return false;
//                 }
//             }
//
//
//             String requestPath = request.getRequestURI();
//             log.info("Authentication check for path: {}", requestPath);
//
//             // 检查方法级别的注解（优先级最高）
//             AuthCheckResult annotationResult = checkAnnotations(handler, requestPath);
//             if (annotationResult != AuthCheckResult.CONTINUE) {
//                 return handleAuthResult(annotationResult, response, requestPath);
//             }
//
//             // 检查是否在白名单中
//             if (isPathInWhitelist(requestPath)) {
//                 log.info("Path in whitelist, allowing access: {}", requestPath);
//                 return true;
//             }
//
//             // 检查用户是否已登录
//             Optional<FmkUserId> userIdOptional = FmkContextUtil.getUserId();
//             if (userIdOptional.isEmpty()) {
//                 log.warn("User not logged in for path: {}", requestPath);
//                 handleUnauthorized(response, "用户未登录，请先登录", "User not logged in, please login first");
//                 return false;
//             }
//
//             FmkUserId userId = userIdOptional.get();
//
//             // 检查管理员接口权限
//             if (isAdminPath(requestPath)) {
//                 boolean checkedRole = fmkAdminUserCheckService.checkRole();
//                 if (checkedRole == false) {
//                     log.warn("Non-admin user trying to access admin path: userId={}, path={}", userId, requestPath);
//                     handleForbidden(response, "权限不足，需要管理员权限", "Insufficient permissions, admin access required");
//                     return false;
//                 }
//                 log.info("Admin access granted: userId={}, path={}", userId, requestPath);
//             }
//
//             log.info("Authentication successful: userId={}, path={}", userId, requestPath);
//             return true;
//
//         } catch (Exception e) {
//             log.error("Authentication check failed for path: {}", request.getRequestURI(), e);
//             return handleException(response, e);
//         }
//     }
//
//     /**
//      * 检查方法和类级别的注解
//      */
//     private AuthCheckResult checkAnnotations(Object handler, String requestPath) {
//         if (!(handler instanceof HandlerMethod)) {
//             return AuthCheckResult.CONTINUE;
//         }
//
//         HandlerMethod handlerMethod = (HandlerMethod) handler;
//
//         // 检查 @NoAuth 注解（方法级别优先）
//         NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
//         if (noAuth == null) {
//             noAuth = handlerMethod.getBeanType().getAnnotation(NoAuth.class);
//         }
//
//         if (noAuth != null) {
//             log.info("@NoAuth annotation found, allowing access: {}", requestPath);
//             return AuthCheckResult.ALLOW;
//         }
//
//         // 检查 @AdminAuth 注解
//         AdminAuth adminAuth = handlerMethod.getMethodAnnotation(AdminAuth.class);
//         if (adminAuth == null) {
//             adminAuth = handlerMethod.getBeanType().getAnnotation(AdminAuth.class);
//         }
//
//         if (adminAuth != null) {
//             log.info("@AdminAuth annotation found, admin access required: {}", requestPath);
//             return AuthCheckResult.REQUIRE_ADMIN;
//         }
//
//         return AuthCheckResult.CONTINUE;
//     }
//
//     /**
//      * 处理认证检查结果
//      */
//     private boolean handleAuthResult(AuthCheckResult result, HttpServletResponse response, String requestPath) {
//         if (result == null) {
//             return true; // 这里保持与 default 分支一致
//         }
//
//         switch (result) {
//             case ALLOW:
//                 return true;
//             case REQUIRE_ADMIN:
//                 Optional<FmkUserId> userIdOptional = FmkContextUtil.getUserId();
//                 if (userIdOptional.isEmpty()) {
//                     try {
//                         handleUnauthorized(response, "用户未登录，请先登录", "User not logged in, please login first");
//                     } catch (IOException e) {
//                         log.error("Failed to send unauthorized response", e);
//                     }
//                     return false;
//                 }
//
//                 boolean checkedRole = fmkAdminUserCheckService.checkRole();
//                 if (checkedRole == false) {
//                     try {
//                         handleForbidden(response, "权限不足，需要管理员权限", "Insufficient permissions, admin access required");
//                     } catch (IOException e) {
//                         log.error("Failed to send forbidden response", e);
//                     }
//                     return false;
//                 }
//                 return true;
//             default:
//                 return true;
//         }
//     }
//
//     /**
//      * 检查路径是否在白名单中
//      */
//     private boolean isPathInWhitelist(String requestPath) {
//         return NO_AUTH_REQUIRED_PATHS.stream()
//                 .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
//     }
//
//     /**
//      * 检查是否是管理员路径
//      */
//     private boolean isAdminPath(String requestPath) {
//         return ADMIN_REQUIRED_PATHS.stream()
//                 .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
//     }
//
//     /**
//      * 处理未授权访问 (401)
//      */
//     private void handleUnauthorized(HttpServletResponse response, String zhMessage, String enMessage) throws IOException {
//         sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, zhMessage, enMessage,
//                 ExceptionDbInterface.AUTHENTICATION_USER.AUTHENTICATION_USER,
//                 ExceptionDbInterface.AUTHENTICATION_USER.MsgValue.TOKEN_EXPIRED);
//     }
//
//     /**
//      * 处理权限不足 (403)
//      */
//     private void handleForbidden(HttpServletResponse response, String zhMessage, String enMessage) throws IOException {
//         sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, zhMessage, enMessage,
//                 ExceptionDbInterface.AUTHENTICATION_USER.AUTHENTICATION_USER,
//                 ExceptionDbInterface.AUTHENTICATION_USER.MsgValue.TOKEN_EXPIRED);
//     }
//
//     /**
//      * 发送错误响应
//      */
//     private void sendErrorResponse(HttpServletResponse response,
//                                    int statusCode,
//                                    String zhMessage,
//                                    String enMessage,
//                                    String msgType,
//                                    String msgKey) throws IOException {
//         response.setStatus(statusCode);
//         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//         response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//         String errorMessage = getErrorMessage(msgType, msgKey, zhMessage, enMessage);
//         FmkResult<Object> result = FmkResult.error(statusCode, errorMessage);
//         String jsonResponse = FmkJacksonUtil.toJson(result);
//
//         response.getWriter().write(jsonResponse);
//         response.getWriter().flush();
//
//         log.info("Error response sent: status={}, message={}", statusCode, errorMessage);
//     }
//
//     /**
//      * 获取错误消息
//      */
//     private String getErrorMessage(String msgType, String msgKey, String zhMessage, String enMessage) {
//         try {
//             if (StringUtils.isNotBlank(zhMessage) && StringUtils.isNotBlank(enMessage)) {
//                 return exceptionService.commonErrorMsg(zhMessage, enMessage);
//             }
//             return exceptionService.errorMsg(msgType, msgKey);
//         } catch (Exception e) {
//             log.warn("Failed to get error message from exception service, using default message", e);
//             return exceptionService.commonErrorMsg(zhMessage, enMessage);
//         }
//     }
//
//     /**
//      * 处理异常情况
//      */
//     private boolean handleException(HttpServletResponse response, Exception e) {
//         try {
//             handleUnauthorized(response, "认证检查失败", "Authentication check failed");
//         } catch (IOException ioException) {
//             log.error("Failed to send error response", ioException);
//         }
//         return false;
//     }
//
// }