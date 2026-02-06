package vlad.corp.money_manager_backend.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Participant;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;
    @Value("${security.jwt.ttl-seconds}")
    private long expirationMs;


    public String generateToken(Participant participant, List<String> roles) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(participant.getId().toString())
                .claim("roles", roles)
                .claim("email", participant.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public JwtPayload validateAndExtract(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        try{
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UUID participantId = UUID.fromString(claims.getSubject());
            String email = claims.get("email", String.class);
            List<String> roles = claims.get("roles", List.class);

            return new JwtPayload(participantId, email, roles);

        } catch (JwtException ex) {
            throw ex;
        }

    }
}
