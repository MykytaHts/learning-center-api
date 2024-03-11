package com.example.learningcenterapi.dto.request;

import com.example.learningcenterapi.domain.enumeration.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDTO {
    @NotNull(message = "Role is not specified.")
    private Role role;
}
