package com.example.learningcenterapi.security.configuration;

import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.security.filter.JWTAuthenticationFilter;
import com.example.learningcenterapi.security.services.JwtService;
import com.example.learningcenterapi.security.utils.OktaConfigurationHolder;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.client.ApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security Configuration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfiguration {
    private final OktaConfigurationHolder oktaAuthHolder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(
            UserDetailsService userDetailsService,
            UnauthorizedAccessHandler unauthorizedAccessHandler,
            HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedAccessHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(jwtService, userDetailsService),
                    UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiClient oktaClient() {
        return Clients.builder()
                .setOrgUrl(oktaAuthHolder.getOktaDomain())
                .setClientCredentials(new TokenClientCredentials(oktaAuthHolder.getOktaToken()))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() ->
                new SystemException("User with email: " + email + " not found.77", UNAUTHORIZED));
    }
}