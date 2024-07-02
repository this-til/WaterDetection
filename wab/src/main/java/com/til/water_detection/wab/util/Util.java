package com.til.water_detection.wab.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.til.water_detection.data.util.FinalString;

import java.util.Date;

public class Util {


    public static String generateJwt() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8))
                .sign(Algorithm.HMAC256(FinalString.START));
    }

    public static void parseJwt(String jwt) {
        JWT.require(Algorithm.HMAC256(FinalString.START))
                .build()
                .verify(jwt);
    }

    public static byte[] trimTrailingZeros(byte[] array) {
        if (array == null || array.length == 0) {
            return array; // 如果数组为空或null，直接返回
        }

        int lastIndex = array.length - 1;
        // 从后往前遍历，找到第一个非0元素的位置
        while (lastIndex >= 0 && array[lastIndex] == 0) {
            lastIndex--;
        }

        // 如果所有元素都是0，则返回一个空数组
        if (lastIndex == -1) {
            return new byte[0];
        }

        // 创建一个新数组，只包含到lastIndex的元素
        byte[] trimmedArray = new byte[lastIndex + 1];
        System.arraycopy(array, 0, trimmedArray, 0, lastIndex + 1);
        return trimmedArray;
    }

}
