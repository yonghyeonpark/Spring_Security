package com.security.service;

import com.security.domain.User;
import com.security.repository.UserRepository;
import com.security.web.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(JoinRequestDto dto) {

        String rawPassword = dto.getPassword();
        String encPassword = encoder.encode(rawPassword);

        User user = User.builder()
                .username(dto.getUsername())
                .password(encPassword)
                .email(dto.getEmail())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

}
