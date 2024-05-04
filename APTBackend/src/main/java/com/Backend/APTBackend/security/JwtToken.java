package com.Backend.APTBackend.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtToken {
    private static final String SECRET_KEY = "hqgmaerhxxgzubhmiadsqirryrydokmunmnxhfcqvhnphgyhbadjwvdlaeivamsmctezuswlwniwqmopmjablztwnlsvfzikkhjwaxynlxebdrgjygiezzzfbqrdqkosjxjkfeikcmcfoyplvgvrqigyxzcyurazvfvrbnayikhrsqtwosxygqyhvbyxldggltirbn";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds

    // @Value("${jwt.secretKey}")
    // public void setSecretKeyForJwt(String secretKey) {
    // SECRET_KEY = secretKey;
    // }

    private static SecretKey getSigningKey() {
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static String generateToken(String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getIdFromToken(String token) {
        try {
            System.out.println("token" + token);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // System.out.println(claims);
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            // Token has expired
            return null;
        } catch (JwtException ex) {
            // Token is invalid or malformed
            return null;
        }
    }
}
