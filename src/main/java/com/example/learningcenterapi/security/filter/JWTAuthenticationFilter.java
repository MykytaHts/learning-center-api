package com.example.learningcenterapi.security.filter;

import com.example.learningcenterapi.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@Log4j2
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("Starting JWT authentication process for request: {}", request.getRequestURI());
        String token = extractToken(request);
        try {
            if (token != null && jwtService.isTokenValid(token)) {
                log.debug("JWT token is valid. Proceeding to extract username.");
                String username = jwtService.extractUsername(token);
                log.debug("Extracted username from JWT token: {}", username);
                authenticateUser(username, request);
            }
        } catch (Exception e) {
            log.error("Error occurred while authenticating user: {}", e.getMessage(), e);
            response.sendError(FORBIDDEN.value(), "Access forbidden. An error occurred during user authentication.");
            return;
        }

        filterChain.doFilter(request, response);
        log.debug("Completed JWT authentication process for request: {}", request.getRequestURI());
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        log.warn("Authorization header is missing or does not start with 'Bearer ' prefix.");
        return null;
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.debug("User '{}' authenticated successfully.", username);
        } else {
            log.warn("Username is null or user is already authenticated.");
        }
    }
}