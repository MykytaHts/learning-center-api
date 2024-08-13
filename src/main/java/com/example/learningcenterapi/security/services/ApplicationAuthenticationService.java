package com.example.learningcenterapi.security.services;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.domain.enumeration.Role;
import com.example.learningcenterapi.dto.response.UserResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.UserMapper;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.security.dto.AuthResponse;
import com.example.learningcenterapi.security.dto.JWTTokenDTO;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApplicationAuthenticationService {

    private final OktaAuthenticationService oktaAuthService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        log.info("Attempting to register user with email {}", registerRequestDTO.getEmail());
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new SystemException("User with this email already exists.", BAD_REQUEST);
        }
        try {
            User user = initializeUserInstance(registerRequestDTO);
            userRepository.save(user);
            oktaAuthService.register(registerRequestDTO);
            log.info("User registration successful for email {}", registerRequestDTO.getEmail());
            return userMapper.toResponseDTO(user);
        } catch (Exception ex) {
            log.error("Error occurred during user registration for email {}: {}",
                    registerRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    public AuthResponse login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("Attempting to login user with email {}", loginRequestDTO.getEmail());
        try {
            AuthResponse authResponse = authenticateAndSetContext(loginRequestDTO, request);
            log.info("User authentication successful for email {}", loginRequestDTO.getEmail());
            return authResponse;
        } catch (Exception ex) {
            log.error("Error occurred during user authentication for email {}: {}",
                    loginRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    public AuthResponse refresh(JWTTokenDTO jwtRefreshDTO, HttpServletRequest request) {
        log.info("Attempting to refresh JWT token for user with refresh token: {}",
                jwtRefreshDTO.getToken());
        final String refreshToken = jwtRefreshDTO.getToken();

        try {
            AuthResponse newAuthResponse = oktaAuthService.refreshAccessToken(refreshToken);
            validateAuthResponse(newAuthResponse);
            String userEmail = jwtService.extractUsername(newAuthResponse.getAccessToken());
            User user = findUserByEmailOrThrow(userEmail);
            setAuthenticationContext(user, request);
            log.info("JWT token successfully refreshed for user: {}", userEmail);
            return newAuthResponse;
        } catch (Exception ex) {
            log.error("Error occurred during token refresh for refresh token {}: {}",
                    refreshToken, ex.getMessage());
            throw ex;
        }
    }

    public void revoke(JWTTokenDTO jwtToken) {
        log.info("Attempting to revoke refresh token: {}", jwtToken);
        try {
            oktaAuthService.revokeAccessToken(jwtToken.getToken());
            log.info("Token revoked successfully.");
        } catch (Exception ex) {
            log.error("Error occurred while revoking token: {}", ex.getMessage());
            throw new SystemException("Failed to revoke token. Please try again.", INTERNAL_SERVER_ERROR);
        }
    }

    private AuthResponse authenticateAndSetContext(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        AuthResponse authResponse = oktaAuthService.authenticate(loginRequestDTO);
        validateAuthResponse(authResponse);

        User user = findUserByEmailOrThrow(loginRequestDTO.getEmail());
        setAuthenticationContext(user, request);
        return authResponse;
    }

    private void setAuthenticationContext(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void validateAuthResponse(AuthResponse authResponse) {
        if (authResponse == null || authResponse.getAccessToken() == null) {
            throw new SystemException("Invalid token or token not found.", UNAUTHORIZED);
        }
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SystemException("User not found in local database", UNAUTHORIZED));
    }

    private User initializeUserInstance(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(registerRequestDTO.getPassword());
        user.setRole(Role.STUDENT);
        return user;
    }
}

