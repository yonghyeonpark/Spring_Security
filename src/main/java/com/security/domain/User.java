package com.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;
    private String role; // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER

    private LocalDateTime createDate;

    @Builder
    public User(String userId, String password, String role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

}
