package com.security.repository;

import com.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // findBy(규칙) + Username(문법)
    // select* from user where username = 매개변수
    public User findByUsername(String username);
}
