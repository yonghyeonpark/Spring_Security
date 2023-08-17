package com.security.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JoinRequestDto {

    private String username;
    private String password;
    private String email;
    private String role;

    @Builder
    public JoinRequestDto(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
