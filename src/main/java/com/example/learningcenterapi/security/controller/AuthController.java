package com.example.learningcenterapi.security.controller;

import com.example.learningcenterapi.security.dto.JWTRefreshDTO;
import com.example.learningcenterapi.security.dto.JWTTokenDTO;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.SignUpRequestDTO;
import com.example.learningcenterapi.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        log.info("REST POST request to register user : {}", signUpRequestDTO);
        authenticationService.register(signUpRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> login(
            @Valid @RequestBody final LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("REST POST request to login user with email : {}", loginRequestDTO.getEmail());
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO, request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTTokenDTO> refreshAccessToken(
            @Valid @RequestBody final JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request) {
        log.info("REST POST request to login refresh access token");
        return ResponseEntity.ok(authenticationService.refresh(jwtRefreshDTO, request));
    }

}
