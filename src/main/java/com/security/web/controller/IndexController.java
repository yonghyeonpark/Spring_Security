package com.security.web.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
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

}
