package com.example.learningcenterapi.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OktaAPI {
    @Value("${idp.okta.domain}")
    private String oktaDomain;

    public String receiveTokenURI() {
        return oktaDomain + "/oauth2/default/v1/token";
    }
    public String refreshTokenURI() {
        return oktaDomain + "/oauth2/default/v1/token";
    }
    public String revokeTokenURI() {
        return oktaDomain + "/oauth2/default/v1/revoke";
    }
    public String oktaIssuerURI() {
        return oktaDomain + "/oauth2/default";
    }
}
