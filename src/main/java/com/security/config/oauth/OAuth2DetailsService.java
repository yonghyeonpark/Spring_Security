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

/** 구글로그인 버튼 클릭 => 구글로그인 창 => 로그인 완료 => code를 리턴(OAuth2-Client 라이브러리) => 엑세스토큰을 요청하고 받음
 * 위의 과정을 거친 후의 정보가 UserRequest임
 * UserRequest를 통해 구글로부터 회원프로필을 받아내는 것이 loadUser 함수
 */

/** 받아낸 회원프로필
 * getAttributes() : {sub=101984376212510965808,
 * name=박용현, given_name=용현, family_name=박,
 * picture=https://lh3.googleusercontent.com/a/AAcHTteeVskDGmFkOfPeHFSDjVCgVI3d5ChQUD8Qlikqb33KEQ=s96-c,
 * email=llyyoo93@gmail.com, email_verified=true, locale=ko}
 */

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> userInfo = oauth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // 어떤 OAuth인지 확인 가능
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
