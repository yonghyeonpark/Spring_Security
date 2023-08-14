package com.security.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JoinRequestDto {

    private String userId;
    private String password;
    private String role;

    @Builder
    public JoinRequestDto(String userId, String password, String role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }
}
