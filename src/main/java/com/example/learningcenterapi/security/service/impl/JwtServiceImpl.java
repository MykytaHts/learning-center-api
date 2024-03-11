package com.example.learningcenterapi.security.service.impl;


import com.example.learningcenterapi.configuration.JwtProperties;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.domain.security.RefreshToken;
import com.example.learningcenterapi.security.repository.RefreshTokenRepository;
import com.example.learningcenterapi.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public String generateJwtAccessToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, new HashMap<>(), true);
    }

    @Override
    @Transactional
    public String generateJwtRefreshToken(User user) {
        refreshTokenRepository.deleteAllByUserId(user.getId());
        refreshTokenRepository.flush();

        LocalDateTime issuedAt  = LocalDateTime.now();
        LocalDateTime expirationDate = issuedAt.plus(jwtProperties.getRefreshTokenExpiration(), ChronoUnit.MILLIS);

        String jwtRefreshToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtRefreshToken);
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Override
    public String generateJwtToken(
            UserDetails userDetails, Map<String, Object> extraClaims, boolean isAccessToken) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenTimeout(isAccessToken)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public boolean isJwtTokenValid(String jwtToken, UserDetails userDetails) {
//        String username = extractUsername(jwtToken);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
        return true;
    }

    @Override
    @Transactional
    public boolean isJwtRefreshTokenValid(String jwtRefreshToken, UserDetails userDetails) {
        return refreshTokenRepository.existsByTokenAndUserEmail(jwtRefreshToken, userDetails.getUsername())
                && isJwtTokenValid(jwtRefreshToken, userDetails);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSigningKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String jwtToken) {
        Date expirationDate = extractExpirationDate(jwtToken);
        return Objects.nonNull(expirationDate) && expirationDate.before(new Date());
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Date extractExpirationDate(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
