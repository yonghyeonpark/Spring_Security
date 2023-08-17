package com.security.web.controller;

import com.security.config.auth.PrincipalDetails;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public @ResponseBody String basic() {
        return "기본 페이지 입니다.";
    }

    // 특정 메서드에 간단하게 접근 권한 지정
    @Secured("ROLE_USER")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "data";
    }

    // 특정 메서드에 여러 접근 권한 지정
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data2")
    public @ResponseBody String data2() {
        return "data2";
    }

    // 스프링 시큐리티는 따로 세션을 가지고 있다.
    // 세션에 들어갈 수 있는 타입은 Authentication 객체 밖에 없다. (UserDetails, OAuth2User)

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("userDetails : " + userDetails.getUser());

        return "세션 정보를 확인합니다.";
    }

    @GetMapping("/test/oauthLogin")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {

        OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();
        System.out.println("authentication : " + oauth2User.getAttributes());

        System.out.println("oauth2User : " + oauth.getAttributes());

        return "세션 정보를 확인합니다.";
    }

}
