package com.rccl.demo.web.security.util;

import com.rccl.demo.web.service.type.ProviderType;
import com.rccl.demo.web.util.DateUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final byte[] signingKey;
    private final int expirationSeconds;

    public JwtUtil(@Value("${rccl.security.jwt.secret}") String secret, @Value("${rccl.security.jwt.expiration-seconds}") int expirationSeconds) {
        this.signingKey = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationSeconds;
    }

    public String generateJwtToken(Authentication authentication, ProviderType providerType) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expirationTime = issuedAt.plusSeconds(this.expirationSeconds);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(DateUtil.withZeroUTC(issuedAt))
                .setExpiration(DateUtil.withZeroUTC(expirationTime))
                .addClaims(providerType.getJwtClaims())
                .claim("roles", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .signWith(Keys.hmacShaKeyFor(this.signingKey), SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication parseJwtToken(String token) {
        try {
            Jws<Claims> parsedToken = Jwts.parser().setSigningKey(this.signingKey).parseClaimsJws(token);

            String username = parsedToken.getBody().getSubject();
            List<GrantedAuthority> authorities = ((List<String>)parsedToken.getBody().get("roles"))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        } catch (Exception e) {
            log.error("::: Unexpected authentication error", e);
        }

        // TODO: handle this properly
        throw new RuntimeException("Unexpected error");
    }
}
