package com.team12.finalproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtils {

    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName",userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

    public static String createRefreshToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName",userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token,secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUserNAme(String token, String secretKey) {
        return extractClaims(token, secretKey).get("userName").toString();
    }
}
