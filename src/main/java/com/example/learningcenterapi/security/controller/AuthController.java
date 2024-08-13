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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("REST POST request to register user : {}", registerRequestDTO);
        return ResponseEntity.ok(authenticationService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("REST POST request to login user with email : {}", loginRequestDTO.getEmail());
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO, request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshAccessToken(
            @Valid @RequestBody JWTTokenDTO jwtTokenDTO, HttpServletRequest request) {
        log.info("REST POST request to login refresh access token");
        return ResponseEntity.ok(authenticationService.refresh(jwtTokenDTO, request));
    }

    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeToken(
            @Valid @RequestBody final JWTTokenDTO jwtTokenDTO) {
        log.info("REST POST request to login refresh access token");
        authenticationService.revoke(jwtTokenDTO);
        return ResponseEntity.ok().build();
    }
}
