package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 활성화, @PreAuthorize + @PostAuthorize 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login") // 시큐리티가 해당 url이 호출될 때 낚아채서 대신 로그인을 진행해줌, controller에 만들지 않아도 됨
                .defaultSuccessUrl("/"); // 로그인 성공 시 해당 url로 이동, 특정 페이지를 요청한 상태로 로그인하면 요청한 페이지로 넘어감
        return http.build();
    }


}
