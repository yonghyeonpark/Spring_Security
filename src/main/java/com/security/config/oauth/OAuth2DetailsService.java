package com.security.config.oauth;

import com.security.config.auth.PrincipalDetails;
import com.security.config.oauth.provider.GoogleUserInfo;
import com.security.config.oauth.provider.NaverUserInfo;
import com.security.config.oauth.provider.OAuth2UserInfo;
import com.security.domain.User;
import com.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> userInfo = oauth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        if(provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(userInfo);
        }else if(provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(userInfo);
        }

        String provierId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + provierId; // 충돌 방지
        String email = oAuth2UserInfo.getEmail();

        System.out.println("getRegistrationId : " + provider); // 어떤 OAuth인지 확인 가능
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes : " + userInfo); // 이미 사용자 정보가 담아져있기 때문에 엑세스토큰이 필요없음

        User findUser = userRepository.findByUsername(username);

        if(findUser == null) {
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .role("ROLE_USER")
                    .build();

            return new PrincipalDetails(userRepository.save(user), userInfo);
        }

        return new PrincipalDetails(userRepository.save(findUser), userInfo);
    }
}
