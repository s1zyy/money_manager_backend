package vlad.corp.money_manager_backend.infrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.User;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;
    @Value("${security.jwt.ttl-seconds}")
    private long expirationMs;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UUID validateAndGetUserId(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return UUID.fromString(claims.getSubject());
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid token");
        }

    }
}
