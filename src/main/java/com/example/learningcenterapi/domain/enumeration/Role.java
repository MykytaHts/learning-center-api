package com.example.learningcenterapi.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("Administrator"),
    INSTRUCTOR("Instructor"),
    STUDENT("Student");

    private final String title;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }
}