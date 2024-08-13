package com.example.learningcenterapi.security.services;

import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.security.utils.OktaAPI;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerifiers;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String DEFAULT_AUDIENCE = "api://default";
    private final AccessTokenVerifier jwtVerifier;

    public JwtService(OktaAPI oktaAPI) {
        this.jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(oktaAPI.oktaIssuerURI())
                .setAudience(DEFAULT_AUDIENCE)
                .build();
    }

    public Jwt verifyToken(String token) {
        try {
            return jwtVerifier.decode(token);
        } catch (Exception e) {
            throw new SystemException("Provided JWT token is invalid", e);
        }
    }

    public String extractUsername(String token) {
        Jwt jwt = verifyToken(token);
        return jwt.getClaims().get("sub").toString();
    }

    public boolean isTokenValid(String token) {
        try {
            verifyToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}