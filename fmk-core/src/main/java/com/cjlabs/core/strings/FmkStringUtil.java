package com.cjlabs.core.strings;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类，封装常用的字符串操作方法
 * 主要基于Apache Commons Lang的StringUtils和其他第三方库
 */
public class FmkStringUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    /**
     * 私有构造函数，防止实例化
     */
    private FmkStringUtil() {
        throw new UnsupportedOperationException("工具类不支持实例化");
    }

    // ====================== 基础判断 ======================

    /**
     * 检查字符串是否为空或null
     *
     * @param str 待检查的字符串
     * @return 如果字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 检查字符串是否为空白字符串
     *
     * @param str 待检查的字符串
     * @return 如果字符串为null、空字符串或只包含空白字符，返回true
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 检查字符串是否不为空且不为null
     *
     * @param str 待检查的字符串
     * @return 如果字符串不为null且不为空字符串，返回true
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 检查字符串是否不为空白字符串
     *
     * @param str 待检查的字符串
     * @return 如果字符串不为null、不为空字符串且不只包含空白字符，返回true
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 检查所有字符串是否都不为空白字符串
     *
     * @param strings 待检查的字符串数组
     * @return 如果所有字符串都不为空白字符串，返回true
     */
    public static boolean isNoneBlank(String... strings) {
        return StringUtils.isNoneBlank(strings);
    }

    /**
     * 检查所有字符串是否都为空白字符串
     *
     * @param strings 待检查的字符串数组
     * @return 如果所有字符串都为空白字符串，返回true
     */
    public static boolean isAllBlank(String... strings) {
        return StringUtils.isAllBlank(strings);
    }

    // ====================== 字符串处理 ======================

    /**
     * 安全地获取字符串，如果为null则返回空字符串
     *
     * @param str 原始字符串
     * @return 非null的字符串
     */
    public static String defaultString(String str) {
        return StringUtils.defaultString(str);
    }

    /**
     * 安全地获取字符串，如果为null则返回默认值
     *
     * @param str          原始字符串
     * @param defaultValue 默认值
     * @return 非null的字符串
     */
    public static String defaultIfNull(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 安全地获取字符串，如果为空白字符串则返回默认值
     *
     * @param str          原始字符串
     * @param defaultValue 默认值
     * @return 非空白的字符串
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return StringUtils.defaultIfBlank(str, defaultValue);
    }

    /**
     * 截取字符串，防止越界
     *
     * @param str    原始字符串
     * @param maxLen 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLen) {
        return StringUtils.truncate(str, maxLen);
    }

    /**
     * 截取字符串并添加省略号
     *
     * @param str    原始字符串
     * @param maxLen 最大长度
     * @return 截取后的字符串，如果被截取则添加省略号
     */
    public static String abbreviate(String str, int maxLen) {
        return StringUtils.abbreviate(str, maxLen);
    }

    /**
     * 去除字符串两端的空白字符
     *
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    /**
     * 去除字符串中的所有空白字符
     *
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String deleteWhitespace(String str) {
        return StringUtils.deleteWhitespace(str);
    }

    /**
     * 将字符串转换为小写
     *
     * @param str 原始字符串
     * @return 转换后的字符串
     */
    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    /**
     * 将字符串转换为大写
     *
     * @param str 原始字符串
     * @return 转换后的字符串
     */
    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }

    /**
     * 将字符串的首字母转换为大写
     *
     * @param str 原始字符串
     * @return 转换后的字符串
     */
    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    /**
     * 将字符串的首字母转换为小写
     *
     * @param str 原始字符串
     * @return 转换后的字符串
     */
    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    /**
     * 将每个单词的首字母转换为大写
     *
     * @param str 原始字符串
     * @return 转换后的字符串
     */
    public static String capitalizeWords(String str) {
        return WordUtils.capitalize(str);
    }

    // ====================== 字符串操作 ======================

    /**
     * 检查字符串是否包含指定的子字符串
     *
     * @param str       原始字符串
     * @param searchStr 子字符串
     * @return 如果包含子字符串，返回true
     */
    public static boolean contains(String str, String searchStr) {
        return Strings.CS.contains(str, searchStr);
    }

    /**
     * 检查字符串是否包含指定的子字符串（忽略大小写）
     *
     * @param str       原始字符串
     * @param searchStr 子字符串
     * @return 如果包含子字符串，返回true
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        return Strings.CI.contains(str, searchStr);
    }

    /**
     * 检查字符串是否以指定的前缀开始
     *
     * @param str    原始字符串
     * @param prefix 前缀
     * @return 如果以指定前缀开始，返回true
     */
    public static boolean startsWith(String str, String prefix) {
        return Strings.CS.startsWith(str, prefix);
    }

    /**
     * 检查字符串是否以指定的前缀开始（忽略大小写）
     *
     * @param str    原始字符串
     * @param prefix 前缀
     * @return 如果以指定前缀开始，返回true
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return Strings.CI.startsWith(str, prefix);
    }

    /**
     * 检查字符串是否以指定的后缀结束
     *
     * @param str    原始字符串
     * @param suffix 后缀
     * @return 如果以指定后缀结束，返回true
     */
    public static boolean endsWith(String str, String suffix) {
        return Strings.CS.endsWith(str, suffix);
    }

    /**
     * 检查字符串是否以指定的后缀结束（忽略大小写）
     *
     * @param str    原始字符串
     * @param suffix 后缀
     * @return 如果以指定后缀结束，返回true
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return Strings.CI.endsWith(str, suffix);
    }

    /**
     * 替换字符串中的指定子字符串
     *
     * @param str        原始字符串
     * @param oldPattern 要替换的子字符串
     * @param newPattern 替换后的子字符串
     * @return 替换后的字符串
     */
    public static String replace(String str, String oldPattern, String newPattern) {
        return Strings.CS.replace(str, oldPattern, newPattern);
    }

    /**
     * 替换字符串中的所有指定子字符串（正则表达式）
     *
     * @param str         原始字符串
     * @param regex       正则表达式
     * @param replacement 替换后的子字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String str, String regex, String replacement) {
        return str != null ? str.replaceAll(regex, replacement) : null;
    }

    // ====================== 字符串生成 ======================

    /**
     * 生成UUID（无连字符）
     *
     * @return UUID字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带连字符的UUID
     *
     * @return 带连字符的UUID字符串
     */
    public static String uuidWithHyphens() {
        return UUID.randomUUID().toString();
    }

    // ====================== 字符串验证 ======================

    /**
     * 检查字符串是否是有效的电子邮件地址
     *
     * @param email 电子邮件地址
     * @return 如果是有效的电子邮件地址，返回true
     */
    public static boolean isValidEmail(String email) {
        return isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 检查字符串是否是有效的URL
     *
     * @param url URL
     * @return 如果是有效的URL，返回true
     */
    public static boolean isValidUrl(String url) {
        return isNotBlank(url) && URL_PATTERN.matcher(url).matches();
    }

    /**
     * 检查字符串是否只包含数字
     *
     * @param str 待检查的字符串
     * @return 如果只包含数字，返回true
     */
    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    /**
     * 检查字符串是否只包含字母
     *
     * @param str 待检查的字符串
     * @return 如果只包含字母，返回true
     */
    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    /**
     * 检查字符串是否只包含字母和数字
     *
     * @param str 待检查的字符串
     * @return 如果只包含字母和数字，返回true
     */
    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }

    // ====================== 字符串转换 ======================

    /**
     * 将字符串转换为字节数组
     *
     * @param str 原始字符串
     * @return 字节数组
     */
    public static byte[] toBytes(String str) {
        return isEmpty(str) ? new byte[0] : str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 将字节数组转换为字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String fromBytes(byte[] bytes) {
        return bytes == null || bytes.length == 0 ? "" : new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    public static String camelToUnderscore(String str) {
        return StringUtils.uncapitalize(
                StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(str), '_')
        ).toLowerCase();
    }

    /**
     * 将下划线命名转换为驼峰命名
     *
     * @param str 下划线命名的字符串
     * @return 驼峰命名的字符串
     */
    public static String underscoreToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }

        String[] parts = str.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                camelCase.append(parts[i].substring(0, 1).toUpperCase());
                if (parts[i].length() > 1) {
                    camelCase.append(parts[i].substring(1).toLowerCase());
                }
            }
        }

        return camelCase.toString();
    }

    /**
     * 将下划线命名转换为帕斯卡命名（首字母大写的驼峰命名）
     *
     * @param str 下划线命名的字符串
     * @return 帕斯卡命名的字符串
     */
    public static String underscoreToPascal(String str) {
        String camel = underscoreToCamel(str);
        return isEmpty(camel) ? camel : StringUtils.capitalize(camel);
    }

    // ====================== 集合操作 ======================

    /**
     * 将集合中的元素连接为字符串
     *
     * @param collection 集合
     * @param delimiter  分隔符
     * @return 连接后的字符串
     */
    public static String join(Collection<?> collection, String delimiter) {
        return StringUtils.join(collection, delimiter);
    }

    /**
     * 将数组中的元素连接为字符串
     *
     * @param array     数组
     * @param delimiter 分隔符
     * @return 连接后的字符串
     */
    public static String join(Object[] array, String delimiter) {
        return StringUtils.join(array, delimiter);
    }

    /**
     * 将字符串按分隔符分割为数组
     *
     * @param str       原始字符串
     * @param delimiter 分隔符
     * @return 分割后的数组
     */
    public static String[] split(String str, String delimiter) {
        return StringUtils.split(str, delimiter);
    }

    // ====================== 特殊处理 ======================

    /**
     * 移除字符串中的重音符号（例如：é -> e）
     *
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String stripAccents(String str) {
        return StringUtils.stripAccents(str);
    }

    /**
     * 反转字符串
     *
     * @param str 原始字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    /**
     * 计算字符串中指定子字符串的出现次数
     *
     * @param str    原始字符串
     * @param subStr 子字符串
     * @return 出现次数
     */
    public static int countMatches(String str, String subStr) {
        return StringUtils.countMatches(str, subStr);
    }

    /**
     * 获取字符串的字节长度（UTF-8编码）
     *
     * @param str 原始字符串
     * @return 字节长度
     */
    public static int getByteLength(String str) {
        return isEmpty(str) ? 0 : str.getBytes(StandardCharsets.UTF_8).length;
    }

    /**
     * 将字符串中的HTML特殊字符转义
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml4(str);
    }

    /**
     * 将转义的HTML字符还原
     *
     * @param str 转义的字符串
     * @return 还原后的字符串
     */
    public static String unescapeHtml(String str) {
        return StringEscapeUtils.unescapeHtml4(str);
    }

    /**
     * 将字符串填充到指定长度
     *
     * @param str     原始字符串
     * @param size    目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String leftPad(String str, int size, char padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }

    /**
     * 将字符串右侧填充到指定长度
     *
     * @param str     原始字符串
     * @param size    目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String rightPad(String str, int size, char padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

    /**
     * 居中填充字符串
     *
     * @param str     原始字符串
     * @param size    目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String center(String str, int size, char padChar) {
        return StringUtils.center(str, size, padChar);
    }

    /**
     * 移除字符串两端的指定字符
     *
     * @param str   原始字符串
     * @param chars 要移除的字符
     * @return 处理后的字符串
     */
    public static String strip(String str, String chars) {
        return StringUtils.strip(str, chars);
    }

    /**
     * 判断字符串是否全部为大写
     *
     * @param str 原始字符串
     * @return 如果全部为大写，返回true
     */
    public static boolean isAllUpperCase(String str) {
        return StringUtils.isAllUpperCase(str);
    }

    /**
     * 判断字符串是否全部为小写
     *
     * @param str 原始字符串
     * @return 如果全部为小写，返回true
     */
    public static boolean isAllLowerCase(String str) {
        return StringUtils.isAllLowerCase(str);
    }

    /**
     * 交换字符串中的大小写
     *
     * @param str 原始字符串
     * @return 交换大小写后的字符串
     */
    public static String swapCase(String str) {
        return StringUtils.swapCase(str);
    }
}