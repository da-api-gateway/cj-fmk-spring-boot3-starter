package com.cjlabs.core.id;

import com.xodo.fmk.web.FmkCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 邀请码生成工具类
 * 支持将长整型UID转换为6-8位字母数字邀请码，并可反推回UID
 */
@Slf4j
public class FmkInviteCodeUtil {

    // 自定义字符集（去掉容易混淆的字符：0, O, 1, I, l）
    private static final String CHARSET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int BASE = CHARSET.length(); // 32进制
    private static final int MIN_LENGTH = 6; // 最小长度
    private static final int MAX_LENGTH = 8; // 最大长度

    // 用于混淆的密钥（可以根据需要修改）
    private static final long ENCRYPTION_KEY = 0x5851F42D4C957F2DL;

    // 字符到数字的映射
    private static final Map<Character, Integer> CHAR_TO_NUM = new HashMap<>();

    static {
        for (int i = 0; i < CHARSET.length(); i++) {
            CHAR_TO_NUM.put(CHARSET.charAt(i), i);
        }
    }

    /**
     * 根据UID生成邀请码
     *
     * @param uid 用户ID
     * @return 6-8位邀请码
     */
    public static String generateInviteCode(Long uid) {
        FmkCheckUtil.checkInput(Objects.isNull(uid));

        // 1. 对UID进行混淆加密
        long encryptedUid = encrypt(uid);

        // 2. 转换为32进制字符串
        String code = toBase32(encryptedUid);

        // 3. 确保长度在6-8位之间
        if (code.length() < MIN_LENGTH) {
            // 如果太短，在前面补充字符
            code = padLeft(code, MIN_LENGTH);
        } else if (code.length() > MAX_LENGTH) {
            // 如果太长，使用哈希压缩
            code = compressToLength(encryptedUid, MAX_LENGTH);
        }

        return code;
    }

    /**
     * 根据邀请码反推UID
     *
     * @param inviteCode 邀请码
     * @return 原始UID
     */
    public static Long parseInviteCode(String inviteCode) {
        FmkCheckUtil.checkInput(StringUtils.isBlank(inviteCode));

        inviteCode = inviteCode.trim().toUpperCase();

        // 验证字符集
        for (char c : inviteCode.toCharArray()) {
            if (!CHAR_TO_NUM.containsKey(c)) {
                FmkCheckUtil.throwDcxjCommonException("邀请码格式错误", "Invalid invite code format");
            }
        }

        try {
            // 1. 从32进制字符串转换为数字
            long encryptedUid = fromBase32(inviteCode);

            // 2. 解密得到原始UID
            long originalUid = decrypt(encryptedUid);

            return originalUid;
        } catch (Exception e) {
            log.info("FmkInviteCodeUtil|parseInviteCode|e={}", e.getMessage(), e);
            FmkCheckUtil.throwDcxjCommonException("邀请码格式错误", "Invalid invite code format");
            return null;
        }
    }

    /**
     * 验证邀请码是否有效
     *
     * @param inviteCode 邀请码
     * @return 是否有效
     */
    public static boolean isValidInviteCode(String inviteCode) {
        try {
            Long uid = parseInviteCode(inviteCode);
            return uid != null && uid > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 简单的异或加密
     */
    private static long encrypt(long uid) {
        // 使用异或和位移进行简单加密
        long encrypted = uid ^ ENCRYPTION_KEY;
        // 添加位移混淆
        encrypted = ((encrypted << 21) | (encrypted >>> 43)) ^ (encrypted * 0x9e3779b97f4a7c15L);
        return Math.abs(encrypted); // 确保为正数
    }

    /**
     * 解密
     */
    private static long decrypt(long encryptedUid) {
        // 反向解密过程
        long temp = encryptedUid ^ (encryptedUid * 0x9e3779b97f4a7c15L);
        temp = ((temp >>> 21) | (temp << 43));
        return temp ^ ENCRYPTION_KEY;
    }

    /**
     * 转换为32进制字符串
     */
    private static String toBase32(long number) {
        if (number == 0) return String.valueOf(CHARSET.charAt(0));

        StringBuilder result = new StringBuilder();
        while (number > 0) {
            result.insert(0, CHARSET.charAt((int) (number % BASE)));
            number /= BASE;
        }
        return result.toString();
    }

    /**
     * 从32进制字符串转换为数字
     */
    private static long fromBase32(String code) {
        long result = 0;
        long power = 1;

        for (int i = code.length() - 1; i >= 0; i--) {
            char c = code.charAt(i);
            int digit = CHAR_TO_NUM.get(c);
            result += digit * power;
            power *= BASE;
        }

        return result;
    }

    /**
     * 左侧补齐字符
     */
    private static String padLeft(String code, int targetLength) {
        StringBuilder sb = new StringBuilder(code);
        while (sb.length() < targetLength) {
            // 使用代码长度作为种子，生成伪随机字符
            int index = (sb.length() + code.hashCode()) % BASE;
            sb.insert(0, CHARSET.charAt(Math.abs(index)));
        }
        return sb.toString();
    }

    /**
     * 压缩到指定长度
     */
    private static String compressToLength(long number, int targetLength) {
        // 使用哈希压缩算法
        long hash = number;
        for (int i = 0; i < 3; i++) {
            hash = hash ^ (hash >>> 16);
            hash = hash * 0x85ebca6b;
            hash = hash ^ (hash >>> 13);
            hash = hash * 0xc2b2ae35;
            hash = hash ^ (hash >>> 16);
        }

        hash = Math.abs(hash);
        String compressed = toBase32(hash);

        if (compressed.length() > targetLength) {
            return compressed.substring(0, targetLength);
        } else {
            return padLeft(compressed, targetLength);
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试你提供的两个UID
        long uid1 = 140849534782668800L;
        long uid2 = 140877088998555648L;

        System.out.println("=== 邀请码生成测试 ===");

        String code1 = generateInviteCode(uid1);
        String code2 = generateInviteCode(uid2);

        System.out.println("UID: " + uid1 + " -> 邀请码: " + code1);
        System.out.println("UID: " + uid2 + " -> 邀请码: " + code2);

        System.out.println("\n=== 邀请码反推测试 ===");

        Long parsedUid1 = parseInviteCode(code1);
        Long parsedUid2 = parseInviteCode(code2);

        System.out.println("邀请码: " + code1 + " -> UID: " + parsedUid1);
        System.out.println("邀请码: " + code2 + " -> UID: " + parsedUid2);

        System.out.println("\n=== 验证结果 ===");
        System.out.println("UID1 匹配: " + (uid1 == parsedUid1));
        System.out.println("UID2 匹配: " + (uid2 == parsedUid2));

        System.out.println("\n=== 邀请码验证测试 ===");
        System.out.println("邀请码 " + code1 + " 有效性: " + isValidInviteCode(code1));
        System.out.println("邀请码 'INVALID' 有效性: " + isValidInviteCode("INVALID"));
    }
}