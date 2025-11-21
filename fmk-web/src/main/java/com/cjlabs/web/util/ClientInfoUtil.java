package com.cjlabs.web.util;


import com.cjlabs.core.strings.FmkStringUtil;
import com.cjlabs.web.threadlocal.ClientInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端信息工具类
 */
@Slf4j
public class ClientInfoUtil {

    // User-Agent 解析的正则表达式
    private static final Pattern MOBILE_PATTERN = Pattern.compile("Mobile|Android|iPhone|iPad|iPod|BlackBerry|Windows Phone", Pattern.CASE_INSENSITIVE);
    private static final Pattern APP_PATTERN = Pattern.compile("(\\w+App|Native|ReactNative|Flutter|Cordova|PhoneGap)", Pattern.CASE_INSENSITIVE);
    private static final Pattern CHROME_PATTERN = Pattern.compile("Chrome/([\\d.]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern FIREFOX_PATTERN = Pattern.compile("Firefox/([\\d.]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SAFARI_PATTERN = Pattern.compile("Version/([\\d.]+).*Safari", Pattern.CASE_INSENSITIVE);
    private static final Pattern EDGE_PATTERN = Pattern.compile("Edg?(?:e)?/([\\d.]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern ANDROID_PATTERN = Pattern.compile("Android ([\\d.]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern IOS_PATTERN = Pattern.compile("OS ([\\d_]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern WINDOWS_PATTERN = Pattern.compile("Windows NT ([\\d.]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MAC_PATTERN = Pattern.compile("Mac OS X ([\\d_]+)", Pattern.CASE_INSENSITIVE);

    // API 客户端标识
    private static final Pattern API_CLIENT_PATTERN = Pattern.compile(
            "(curl|wget|HTTPie|Postman|Insomnia|Axios|OkHttp|Java|Python|Go-http-client|Apache-HttpClient)",
            Pattern.CASE_INSENSITIVE
    );

    // 已知的代理头字段
    private static final String[] PROXY_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "X-Original-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED"
    };

    /**
     * 获取客户端真实IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        // 按优先级检查代理头
        for (String header : PROXY_HEADERS) {
            String ip = getValidIpFromHeader(request, header);
            if (ip != null) {
                return ip;
            }
        }

        // 最后取 remote address
        String remoteAddr = request.getRemoteAddr();
        return FmkStringUtil.isNotBlank(remoteAddr) ? remoteAddr : "unknown";
    }

    /**
     * 从指定头字段获取有效IP
     */
    private static String getValidIpFromHeader(HttpServletRequest request, String headerName) {
        String headerValue = request.getHeader(headerName);
        if (FmkStringUtil.isBlank(headerValue) || "unknown".equalsIgnoreCase(headerValue)) {
            return null;
        }

        // 处理多级代理的情况，第一个IP为客户端真实IP
        int index = headerValue.indexOf(",");
        String ip = index != -1 ? headerValue.substring(0, index).trim() : headerValue.trim();

        // 验证IP格式（简单验证）
        return isValidIp(ip) ? ip : null;
    }

    /**
     * 简单IP格式验证
     */
    private static boolean isValidIp(String ip) {
        if (FmkStringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        // 简单检查是否包含数字和点号（IPv4）或冒号（IPv6）
        return ip.matches("^[\\d.:a-fA-F]+$") && !ip.equals("0.0.0.0") && !ip.equals("127.0.0.1");
    }

    /**
     * 解析 User-Agent 获取设备和浏览器信息
     */
    public static void parseUserAgent(String userAgent, ClientInfo clientInfo) {
        if (FmkStringUtil.isBlank(userAgent) || clientInfo == null) {
            return;
        }

        try {
            // 设置原始 User-Agent
            clientInfo.setUserAgent(userAgent);

            // 判断设备类型
            // DeviceTypeEnum deviceType = parseDeviceType(userAgent);
            // clientInfo.setDeviceType(deviceType);

            // 解析操作系统
            String operatingSystem = parseOperatingSystem(userAgent);
            clientInfo.setOperatingSystem(operatingSystem);

            // 解析浏览器
            parseBrowser(userAgent, clientInfo);

            log.info("ClientInfoUtil|parseUserAgent|解析完成|os={}|browser={}", operatingSystem, clientInfo.getBrowser());

        } catch (Exception e) {
            log.warn("ClientInfoUtil|parseUserAgent|解析User-Agent失败|userAgent={}", userAgent, e);
        }
    }

    // /**
    //  * 解析设备类型，返回 DeviceTypeEnum
    //  */
    // public static DeviceTypeEnum parseDeviceType(String userAgent) {
    //     if (StringUtils.isBlank(userAgent)) {
    //         return DeviceTypeEnum.WEB;
    //     }
    //
    //     // 检查是否为API客户端（优先级最高）
    //     if (API_CLIENT_PATTERN.matcher(userAgent).find() ||
    //             userAgent.toLowerCase().contains("api")) {
    //         return DeviceTypeEnum.API;
    //     }
    //
    //     // 检查是否为原生应用
    //     if (APP_PATTERN.matcher(userAgent).find() ||
    //             userAgent.contains("App/") ||
    //             userAgent.contains("Mobile App") ||
    //             userAgent.contains("Native")) {
    //         return DeviceTypeEnum.APP;
    //     }
    //
    //     // 检查是否为桌面应用
    //     if (userAgent.contains("Electron") ||
    //             userAgent.contains("nwjs") ||
    //             userAgent.contains("Desktop")) {
    //         return DeviceTypeEnum.DESKTOP;
    //     }
    //
    //     // 检查是否为移动设备web
    //     if (MOBILE_PATTERN.matcher(userAgent).find()) {
    //         return DeviceTypeEnum.MOBILE;
    //     }
    //
    //     // 默认为 WEB
    //     return DeviceTypeEnum.WEB;
    // }

    /**
     * 解析操作系统
     */
    public static String parseOperatingSystem(String userAgent) {
        if (FmkStringUtil.isBlank(userAgent)) {
            return "Unknown";
        }

        Matcher matcher;

        // Android
        matcher = ANDROID_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            return "Android " + matcher.group(1);
        }

        // iOS
        matcher = IOS_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String version = matcher.group(1).replace("_", ".");
            return "iOS " + version;
        }

        // Windows
        matcher = WINDOWS_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String version = matcher.group(1);
            return "Windows " + getWindowsVersion(version);
        }

        // macOS
        matcher = MAC_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String version = matcher.group(1).replace("_", ".");
            return "macOS " + version;
        }

        // 其他系统
        if (userAgent.toLowerCase().contains("linux")) {
            return "Linux";
        }

        if (userAgent.toLowerCase().contains("unix")) {
            return "Unix";
        }

        return "Unknown";
    }

    /**
     * 解析浏览器
     */
    public static void parseBrowser(String userAgent, ClientInfo clientInfo) {
        if (FmkStringUtil.isBlank(userAgent) || clientInfo == null) {
            clientInfo.setBrowser("Unknown");
            clientInfo.setBrowserVersion("Unknown");
            return;
        }

        Matcher matcher;

        // Edge (需要在 Chrome 之前检查，因为 Edge 也包含 Chrome)
        matcher = EDGE_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            clientInfo.setBrowser("Edge");
            clientInfo.setBrowserVersion(matcher.group(1));
            return;
        }

        // Chrome
        matcher = CHROME_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            clientInfo.setBrowser("Chrome");
            clientInfo.setBrowserVersion(matcher.group(1));
            return;
        }

        // Firefox
        matcher = FIREFOX_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            clientInfo.setBrowser("Firefox");
            clientInfo.setBrowserVersion(matcher.group(1));
            return;
        }

        // Safari
        matcher = SAFARI_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            clientInfo.setBrowser("Safari");
            clientInfo.setBrowserVersion(matcher.group(1));
            return;
        }

        // 其他浏览器
        clientInfo.setBrowser("Unknown");
        clientInfo.setBrowserVersion("Unknown");
    }

    /**
     * Windows 版本号映射
     */
    private static String getWindowsVersion(String ntVersion) {
        switch (ntVersion) {
            case "10.0":
                return "10/11";  // Windows 10 和 11 都是 NT 10.0
            case "6.3":
                return "8.1";
            case "6.2":
                return "8";
            case "6.1":
                return "7";
            case "6.0":
                return "Vista";
            case "5.1":
                return "XP";
            case "5.0":
                return "2000";
            default:
                return ntVersion;
        }
    }

    // /**
    //  * 从设备类型字符串转换为枚举
    //  */
    // public static DeviceTypeEnum parseDeviceTypeFromString(String deviceTypeStr) {
    //     if (StringUtils.isBlank(deviceTypeStr)) {
    //         return DeviceTypeEnum.WEB;
    //     }
    //
    //     // 尝试直接匹配枚举值
    //     try {
    //         return DeviceTypeEnum.valueOf(deviceTypeStr.toUpperCase());
    //     } catch (IllegalArgumentException e) {
    //         // 如果直接匹配失败，进行模糊匹配
    //         String upperStr = deviceTypeStr.toUpperCase();
    //
    //         if (upperStr.contains("API") || upperStr.contains("CURL") ||
    //                 upperStr.contains("POSTMAN") || upperStr.contains("CLIENT")) {
    //             return DeviceTypeEnum.API;
    //         } else if (upperStr.contains("APP") || upperStr.contains("NATIVE") ||
    //                 upperStr.contains("MOBILE APP")) {
    //             return DeviceTypeEnum.APP;
    //         } else if (upperStr.contains("MOBILE") || upperStr.contains("ANDROID") ||
    //                 upperStr.contains("IOS") || upperStr.contains("IPHONE")) {
    //             return DeviceTypeEnum.MOBILE;
    //         } else if (upperStr.contains("DESKTOP") || upperStr.contains("ELECTRON")) {
    //             return DeviceTypeEnum.DESKTOP;
    //         } else {
    //             return DeviceTypeEnum.WEB;
    //         }
    //     }
    // }
}