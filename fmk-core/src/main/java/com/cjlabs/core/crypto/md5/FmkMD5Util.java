package com.cjlabs.core.crypto.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FmkMD5Util {

    /**
     * 加密密码 + 盐值，返回32位小写MD5字符串
     */
    public static String encrypt(String password, String salt) {
        String combined = password + salt;
        return md5(combined);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String inputPassword, String salt, String storedHash) {
        String hash = encrypt(inputPassword, salt);
        return hash.equalsIgnoreCase(storedHash);
    }

    /**
     * 原始 MD5 加密（32位小写）
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 计算字节数组的MD5值（32位小写）
     */
    public static String md5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input);
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * byte数组转16进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b); // 保证正数
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
