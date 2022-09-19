package com.ktj.security1.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ktj.security1.auth.PrincipalDetails;
import com.ktj.security1.model.User;
import com.ktj.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;

	// 함수가 종료시에 @AuthenticationPrincipal 이노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest getAccessToken :" + userRequest.getAccessToken().getTokenValue());
		System.out.println("userRequest getClientRegistration:" + userRequest.getClientRegistration());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		//구글로그인 버튼 클릭->구글로그인창->로그인완료->code를 리턴(Oauth2-client라이버리)->AccessToken요청
		//userRequest정보 -> loadUser함수 호출->구글로부터 사용자 프로필을 받아준다. 
		System.out.println("userRequest getAttributes:" + oAuth2User.getAttributes());	
		
		String provider = userRequest.getClientRegistration().getClientId();//google
		String providerId = oAuth2User.getAttribute("sub"); //112558016486695631215
		String username = provider + "_" + providerId; // google_112558016486695631215
		//String password = bCryptPasswordEncoder.encode("1234");
		String role = "ROLE_USER";
		String email = oAuth2User.getAttribute("email");
		
		User userEntity =  userRepository.findByUsername(username);
		//자동 회원 가입
		if(userEntity==null) {
			
			userEntity = new User();
			userEntity.setUsername(username);
			userEntity.setProvider(providerId);
			userEntity.setProviderId(providerId);
			userEntity.setRole(role);
			userEntity.setEmail(email);
			
			userRepository.save(userEntity);
			
		}
		
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
	
}
