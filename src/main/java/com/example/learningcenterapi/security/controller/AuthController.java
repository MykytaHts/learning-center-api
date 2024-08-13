package com.example.learningcenterapi.security.controller;

import com.example.learningcenterapi.dto.response.UserResponseDTO;
import com.example.learningcenterapi.security.dto.AuthResponse;
import com.example.learningcenterapi.security.dto.JWTTokenDTO;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.RegisterRequestDTO;
import com.example.learningcenterapi.security.services.ApplicationAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    private final ApplicationAuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param registerRequestDTO The RegisterRequestDTO object containing the user's registration information.
     *                           It should not be null or empty.
     * @return A ResponseEntity with UserResponseDTO as the response body, indicating a successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("REST POST request to register user : {}", registerRequestDTO);
        return ResponseEntity.ok(authenticationService.register(registerRequestDTO));
    }

    /**
     * Logs in the user with the provided login request and returns the authentication response.
     *
     * @param loginRequestDTO The login request DTO containing the user's email and password.
     * @param request         The HttpServletRequest object.
     * @return The ResponseEntity containing the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("REST POST request to login user with email : {}", loginRequestDTO.getEmail());
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO, request));
    }

    /**
     * Refreshes the access token for the authenticated user.
     *
     * @param jwtTokenDTO The JWTTokenDTO containing the refresh token.
     * @param request The HttpServletRequest object.
     * @return ResponseEntity with the refreshed access token.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshAccessToken(
            @Valid @RequestBody JWTTokenDTO jwtTokenDTO, HttpServletRequest request) {
        log.info("REST POST request to login refresh access token");
        return ResponseEntity.ok(authenticationService.refresh(jwtTokenDTO, request));
    }

    /**
     * Revokes a token.
     *
     * @param jwtTokenDTO The JWT token to be revoked.
     * @return A ResponseEntity with no content if the token was revoked successfully.
     */
    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeToken(
            @Valid @RequestBody JWTTokenDTO jwtTokenDTO) {
        log.info("REST POST request to login refresh access token");
        authenticationService.revoke(jwtTokenDTO);
        return ResponseEntity.ok().build();
    }
}
