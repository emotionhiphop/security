package com.ktj.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktj.security1.auth.PrincipalDetails;
import com.ktj.security1.model.User;
import com.ktj.security1.repository.UserRepository;

@Controller //view를 리턴
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) {// DI주입 두가지 방식으로 userDetail을 가져올수있다.
		System.out.println("authentication===" + authentication);
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println("principalDetails user name : " + principalDetails.getUsername());
		
		System.out.println("userDetails user name : " + userDetails.getUsername());
		
		return "세션정보 가져오기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {// DI주입 두가지 방식으로 OAuth2User을 가져올수있다.
		System.out.println("authentication===" + authentication);
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println("oAuth2User  : " + oAuth2User.getAttributes());
		
		System.out.println("oAuth2User  : " + oauth.getAttributes());
		return "세션정보 가져오기";
	}
		
	
	@GetMapping({"/", ""})
	public String index() {
		//머스테치 기본 폴더가 src/main/resoureces/
		//viewresolver에 prefix는 /templates/, subfix는 .mustache 
		// (application에 지정하지 않아도 기본 이 설정임.
		return "index"; // src/main/resources/templates/index.mustache 
	}
	
	//일반 로그인, OAuth2User다 사용 가능..
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails ) {
		System.out.println("principalDetails :" + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}	
	
	//spring security가 낙아챔 - securityConfig생성후 작동 안함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public @ResponseBody String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String orgPwd = user.getPassword();
		String encPwd = bCryptPasswordEncoder.encode(orgPwd);
		user.setPassword(encPwd);
		userRepository.save(user);//회원가입은 잘됨. 비밀번호:1234=>security 로그인을 할 수 없음.(비번이 암호화가 안되서)
		return "join";
	}	
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	//@PostAuthorize => 메소드가 종료된후 처리.. 
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "Data정보";
	}
	

}
