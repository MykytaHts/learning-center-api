package com.example.learningcenterapi.security.service.impl;


import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.security.dto.JWTRefreshDTO;
import com.example.learningcenterapi.security.dto.JWTTokenDTO;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.SignUpRequestDTO;
import com.example.learningcenterapi.security.service.AuthenticationService;
import com.example.learningcenterapi.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.learningcenterapi.domain.enumeration.Role.STUDENT;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(SignUpRequestDTO signUpRequestDTO) {
        log.info("Attempting to register user with email {}", signUpRequestDTO.getEmail());
        try {
            if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
                throw new SystemException("Incorrect credentials. Please check email or password.", BAD_REQUEST);
            }
            User user = new User();
            user.setFirstName(signUpRequestDTO.getFirstName());
            user.setLastName(signUpRequestDTO.getLastName());
            user.setEmail(signUpRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
            user.setRole(STUDENT);
            userRepository.save(user);
            log.info("User registration successful for email {}", signUpRequestDTO.getEmail());
        } catch (Exception ex) {
            log.error("Error occurred during user registration for email {}: {}", signUpRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public JWTTokenDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("Attempting to login user with email {}", loginRequestDTO.getEmail());
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            authenticationManager.authenticate(authToken);
            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() ->
                    new SystemException("User with email: " + loginRequestDTO.getEmail() + " not found.", BAD_REQUEST));
            String jwtAccessToken = jwtService.generateJwtAccessToken(user);
            String jwtRefreshToken = jwtService.generateJwtRefreshToken(user);
            log.info("User authentication successful for email {}", loginRequestDTO.getEmail());
            return JWTTokenDTO.builder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
        } catch (Exception ex) {
            log.error("Error occurred during user authentication for email {}: {}", loginRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public JWTTokenDTO refresh(JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request) {
        log.info("Attempting to refresh JWT token for user with refresh token: {}", jwtRefreshDTO.getRefreshToken());
        String refreshToken = jwtRefreshDTO.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);
        if (StringUtils.isBlank(userEmail)) {
            throw new SystemException("Failed to refresh JWT token. User email not found in refresh token.", UNAUTHORIZED);
        }
        User userDetails = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new SystemException("Failed to refresh JWT token. User not found for email: " + userEmail + "", UNAUTHORIZED));
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            if (jwtService.isJwtRefreshTokenValid(refreshToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("JWT token successfully refreshed for user: {}", userEmail);
            } else {
                log.error("Failed to refresh JWT token. Refresh token is not valid for user: {}", userEmail);
                throw new SystemException("Failed to validate JWT refresh token. Please login", FORBIDDEN);
            }
        }
        String jwtAccessToken = jwtService.generateJwtAccessToken(userDetails);
        String jwtRefreshToken = jwtService.generateJwtRefreshToken(userDetails);
        return JWTTokenDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
}
