package com.security.config;

import com.security.service.AuthService;
import com.security.web.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Controller
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;

    // html파일을 사용해도 mustache가 인지한다.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");

        registry.viewResolver(resolver);
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    // 스프링시큐리티가 해당 url 낚아챔 => SecurityConfig 생성 후 낚아채지지 않음
    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(JoinRequestDto dto) {

        authService.join(dto);

        return "redirect:/login";
    }
}
