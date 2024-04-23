package com.til.water_detection.wab.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.til.water_detection.data.User;

import java.util.Date;

public class JwtTokenUtil {

    public static final String KEY = "siberianGiantCat";

    public static String genToken(User user) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 12))
                .sign(Algorithm.HMAC256(KEY));
    }

    public static int parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("id")
                .asInt();
    }

}
