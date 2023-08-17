package com.security.config.auth;

import com.security.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

@Getter
@Setter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 컴포지션
    private Map<String, Object> attributes;


    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
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

    // OAuth2User 오버라이딩

    /**
     * getAttributes() : {sub=101984376212510965808, // google의 primarykey
     * name=박용현, given_name=용현, family_name=박,
     * picture=https://lh3.googleusercontent.com/a/AAcHTteeVskDGmFkOfPeHFSDjVCgVI3d5ChQUD8Qlikqb33KEQ=s96-c,
     * email=llyyoo93@gmail.com, email_verified=true, locale=ko}
     */

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() {
        return null;
    }
}
