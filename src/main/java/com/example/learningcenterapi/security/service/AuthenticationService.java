package com.example.learningcenterapi.security.service;


import com.example.learningcenterapi.security.dto.JWTRefreshDTO;
import com.example.learningcenterapi.security.dto.JWTTokenDTO;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.SignUpRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * An interface for authentication related operations, providing methods for registering, logging in, and refreshing JWT tokens.
 */
public interface AuthenticationService {

    /**
     * Registers a new user with the provided sign-up request data and returns a JWT token.
     *
     * @param signUpRequestDTO the sign-up request data containing user information
     */
    void register(SignUpRequestDTO signUpRequestDTO);

    /**
     * Authenticates a user with the provided login request data and returns a JWT token.
     *
     * @param loginRequestDTO the login request data containing user credentials
     * @param request         the HTTP servlet request
     * @return a JWT token for the authenticated user
     */
    JWTTokenDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request);

    /**
     * Refreshes an expired JWT token with the provided refresh token data and returns a new JWT token.
     *
     * @param jwtRefreshDTO the refresh token data containing the expired JWT and refresh token
     * @param request       the HTTP servlet request
     * @return a new JWT token for the refreshed user session
     */
    JWTTokenDTO refresh(JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request);
}
