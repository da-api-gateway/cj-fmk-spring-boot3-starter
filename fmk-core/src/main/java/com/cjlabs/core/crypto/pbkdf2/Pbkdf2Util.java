package com.cjlabs.core.crypto.pbkdf2;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Pbkdf2Util {

    // 配置参数
    private static final int SALT_LENGTH = 16;         // 盐长度（字节）
    private static final int ITERATIONS = 65536;       // 迭代次数
    private static final int KEY_LENGTH = 256;         // 生成的密钥长度（位）

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * 生成加密后的密码（返回格式：salt:hash）
     */
    public static String hashPassword(String password) {
        byte[] salt = generateSalt();
        byte[] hash = pbkdf2(password.toCharArray(), salt);

        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 校验密码是否匹配
     */
    public static boolean verifyPassword(String rawPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        if (parts.length != 2) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
        byte[] actualHash = pbkdf2(rawPassword.toCharArray(), salt);

        return slowEquals(expectedHash, actualHash);
    }

    /**
     * PBKDF2 计算方法
     */
    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("PBKDF2 hashing failed", e);
        }
    }

    /**
     * 生成随机盐
     */
    private static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * 安全地比较两个字节数组，防止时序攻击
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
