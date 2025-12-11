package com.isa.config;

import com.isa.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private static final String ROLE = "role";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    private static final String SECRET = "my-very-long-super-secret-key-that-is-at-least-64-bytes-long-for-hs512";
    private static final Key secureKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String createAuthenticationToken(long userId, Instant expirationDate, Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(ROLE, role)
                .signWith(secureKey)
                .setExpiration(Date.from(expirationDate))
                .compact();
    }

    public String createRefreshToken(long userId, Instant expirationDate) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(secureKey)
                .setExpiration(Date.from(expirationDate))
                .compact();
    }

    public long getUserId(String token) {
        final Claims claims = getJwtClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public Role getRole(String token) {
        return Role.valueOf(getClaim(token, ROLE, String.class));
    }

    private <T> T getClaim(String token, String claim, Class<T> claimClass) {
        final Claims claims = getJwtClaims(token);
        return claims.get(claim, claimClass);
    }

    public Claims getJwtClaims(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(secureKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }

    public Optional<String> extractToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            final String jwtToken = bearerToken.substring(BEARER.length());
            return Optional.of(jwtToken);
        }
        return Optional.empty();
    }
}
