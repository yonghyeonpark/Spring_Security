package com.security.config;

import com.security.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 소셜 로그인 완료 후
 * 1. 코드를 받음(인증) - OAuth2-client 라이브러리가 받음
 * 2. 코드를 통해 엑세스토큰을 받음(권한)
 * 3. 엑세스토큰을 통해 사용자 프로필 정보를 가져옴
 * 4-1 정보를 토대로 회원가입을 자동으로 진행
 * 4-2 추가로 더 필요한 정보가 있다면 또 다른 회원가입창으로 연결
 *
 * oauth2 client 라이브러리를 사용하면 엑세스토큰과 사용자 프로필 정보를 한 번에 받음
 */

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 활성화, @PreAuthorize + @PostAuthorize 활성화
public class SecurityConfig {

    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/user/**", "/").authenticated() // 인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // 해당 url이 호출될 때 시큐리티가 낚아채서 대신 로그인을 진행해줌, controller에 만들지 않아도 됨
                .defaultSuccessUrl("/") // 로그인 성공 시 해당 url로 이동, 특정 페이지를 요청한 상태로 로그인하면 요청한 페이지로 넘어감
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2DetailsService); // type : OAuth2UserService

        return http.build();
    }
}
