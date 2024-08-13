package com.example.learningcenterapi.security.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OktaConfigurationHolder {
    @Value("${idp.okta.client-id}")
    private String clientId;

    @Value("${idp.okta.client-secret}")
    private String clientSecret;

    @Value("${idp.okta.scope}")
    private String scope;

    @Value("${idp.okta.grant-type}")
    private String grantType;

    @Value("${idp.okta.domain}")
    private String oktaDomain;

    @Value("${idp.okta.token}")
    private String oktaToken;
}
