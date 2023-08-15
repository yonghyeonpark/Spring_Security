package com.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role; // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER

    private LocalDateTime createDate;

    @Builder
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

}
