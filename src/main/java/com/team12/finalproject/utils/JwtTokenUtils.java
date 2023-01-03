package com.team12.finalproject.utils;

import com.team12.finalproject.domain.role.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtils {

    public static String createToken(String userName, UserRole role, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName",userName);
        claims.put("role",String.valueOf(role));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

    public static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(Claims claims, String secretKey) {
        Date expiredDate = claims.getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUserNAme(String token, String secretKey) {
        return extractClaims(token, secretKey).get("userName").toString();
    }

    public static String getUserRole(String token, String secretKey) {
        return extractClaims(token,secretKey).get("role").toString();
    }
}
