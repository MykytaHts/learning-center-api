package com.example.learningcenterapi.dto.response;

import com.example.learningcenterapi.domain.enumeration.Role;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
