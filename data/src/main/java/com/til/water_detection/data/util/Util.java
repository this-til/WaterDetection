package com.til.water_detection.data.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Util {
    private static final SecureRandom random = new SecureRandom();
    private static final   MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成一个随机的盐值
     *
     * @return 生成的盐值
     */
    public static String generateSalt(int length) {
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 使用给定的明文密码和盐值生成加密密码
     *
     * @param plainPassword 明文密码
     * @param salt          盐值
     * @return 加密后的密码
     */
    public static String hashPassword(String plainPassword, String salt)  {
        String saltedPassword = plainPassword + salt;
        byte[] hash = md.digest(saltedPassword.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    public static <T>T cast(Object obj) {
        //noinspection unchecked
        return obj == null ? null : (T)obj;
    }


}
