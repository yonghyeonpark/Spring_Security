package com.security.config.auth;

import com.security.domain.User;
import com.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정에서 .loginProcessingUrl("/login");
 * 이후 login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 메서드가 실행됨
 * 넘어온 parameter username을 가져옴
 */
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            return null;
        }

        return new PrincipalDetails(userEntity);
    }
}
