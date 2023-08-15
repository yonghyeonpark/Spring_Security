package com.security.config.auth;

import com.security.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login 주소 요청을 낚아채서 로그인을 진행시킴
 * 로그인 진행이 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder 라는 키값에 세션을 저장)
 * 세션에 들어갈 수 있는 오브젝트가 정해져 있음 => Authentication 타입 객체
 * Authentication 안에 User 정보가 있어야 됨
 * User오트젝트 타입 => UserDetails 타입 객체
 *
 * UserDetails(PrincipalDetails) => Authentication(UserDetails(PrincipalDetails)) => Security Session(Authentication(UserDetails(PrincipalDetails)))
 * loadUserByUsername이 처리
 */

public class PrincipalDetails implements UserDetails {

    private User user; // 컴포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 람다식
        Collection<GrantedAuthority> collection = new ArrayList();

        collection.add(() -> user.getRole());

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
