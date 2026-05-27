package com.manpowergroup.kintai.framework.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

// JWTトークンの生成・検証・クレーム取得
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final String issuer;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.expire-seconds}") long expireSeconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expirationMs = expireSeconds * 1000L;
    }

    // トークン生成
    public String generateToken(Long employeeId, Long accountId, List<String> roles) {
        return Jwts.builder()
                .setSubject(String.valueOf(employeeId))
                .setIssuer(issuer)
                .addClaims(Map.of(
                        "accountId", accountId,
                        "roles", roles
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // トークン検証
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // employeeId取得
    public Long getEmployeeId(String token) {
        String subject = parseClaims(token).getSubject();
        return subject == null ? null : Long.valueOf(subject);
    }

    // accountId取得
    public Long getAccountId(String token) {
        Object accountId = parseClaims(token).get("accountId");
        return accountId == null ? null : Long.valueOf(String.valueOf(accountId));
    }

    // Claims解析
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
