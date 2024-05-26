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

}
