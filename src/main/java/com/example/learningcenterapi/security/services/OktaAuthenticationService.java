package com.example.learningcenterapi.security.services;

import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.security.dto.AuthResponse;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.security.dto.RegisterRequestDTO;
import com.example.learningcenterapi.security.utils.OktaAPI;
import com.example.learningcenterapi.security.utils.OktaConfigurationHolder;
import com.okta.sdk.resource.api.UserApi;
import com.okta.sdk.resource.client.ApiClient;
import com.okta.sdk.resource.user.UserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class OktaAuthenticationService {
    private final ApiClient oktaClient;
    private final OktaAPI oktaAPI;
    private final OktaConfigurationHolder authConfigHolder;
    private final RestTemplate restTemplate;

    public AuthResponse authenticate(LoginRequestDTO loginDTO) {
        MultiValueMap<String, String> body = generateAccessTokenRequestBody(
                loginDTO.getEmail(), loginDTO.getPassword());
        return executeTokenRequest(body, oktaAPI.receiveTokenURI());
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = generateRefreshTokenRequestBody(refreshToken);
        return executeTokenRequest(body, oktaAPI.refreshTokenURI());
    }

    public void revokeAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = generateRevokeTokenRequestBody(refreshToken);
        executeTokenRevokeRequest(body);
    }

    public void register(RegisterRequestDTO registerDTO) {
         UserApi userApi = new UserApi(oktaClient);
         UserBuilder.instance()
            .setEmail(registerDTO.getEmail())
            .setFirstName(registerDTO.getFirstName())
            .setLastName(registerDTO.getLastName())
            .setLogin(registerDTO.getEmail())
            .setPassword(registerDTO.getPassword().toCharArray())
            .buildAndCreate(userApi);
    }

    private void executeTokenRevokeRequest(MultiValueMap<String, String> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            signRequest(headers);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    oktaAPI.revokeTokenURI(), request, Void.class);
            if (response.getStatusCode().isError()) {
                throw new SystemException("Failed to revoke token. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new SystemException("Token revocation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SystemException("An error occurred while revoking the token: " + e.getMessage(), e);
        }
    }

    private AuthResponse executeTokenRequest(MultiValueMap<String, String> body, String endpoint) {
        try {
            HttpHeaders headers = new HttpHeaders();
            signRequest(headers);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    endpoint, request, AuthResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new SystemException("Token request failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SystemException("An error occurred while processing the token request: " + e.getMessage(), e);
        }
    }

    private MultiValueMap<String, String> generateAccessTokenRequestBody(String username, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", authConfigHolder.getGrantType());
        body.add("username", username);
        body.add("password", password);
        body.add("client_id", authConfigHolder.getClientId());
        body.add("client_secret", authConfigHolder.getClientSecret());
        body.add("scope", authConfigHolder.getScope());
        return body;
    }

    private MultiValueMap<String, String> generateRefreshTokenRequestBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("client_id", authConfigHolder.getClientId());
        body.add("client_secret", authConfigHolder.getClientSecret());
        body.add("scope", authConfigHolder.getScope());
        return body;
    }

    private MultiValueMap<String, String> generateRevokeTokenRequestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        body.add("client_id", authConfigHolder.getClientId());
        body.add("client_secret", authConfigHolder.getClientSecret());
        return body;
    }

    private void signRequest(HttpHeaders requestHeaders) {
        requestHeaders.set("Content-Type", "application/x-www-form-urlencoded");
    }
}
