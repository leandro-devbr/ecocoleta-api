package br.com.ecocoleta.ecocoleta.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(30);
    private final SecretKey signingKey;

    public JwtService(@Value("${security.jwt.secret:}") String encodedSecret) {
        if (encodedSecret.isBlank()) {
            throw new IllegalStateException(
                    "A variável de ambiente JWT_SECRET é obrigatória. Configure uma chave Base64 com pelo menos 32 bytes."
            );
        }

        try {
            this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecret));
        } catch (JwtException | IllegalArgumentException exception) {
            throw new IllegalStateException("JWT_SECRET deve ser uma chave Base64 válida de pelo menos 32 bytes.", exception);
        }
    }

    public String generateToken(UserDetails userDetails) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plus(TOKEN_LIFETIME)))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject() != null
                    && claims.getSubject().equals(userDetails.getUsername())
                    && claims.getExpiration() != null
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public long getTokenLifetimeSeconds() {
        return TOKEN_LIFETIME.toSeconds();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
