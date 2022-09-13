package com.ktj.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktj.security1.model.User;
import com.ktj.security1.repository.UserRepository;

@Controller //view를 리턴
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({"/", ""})
	public String index() {
		//머스테치 기본 폴더가 src/main/resoureces/
		//viewresolver에 prefix는 /templates/, subfix는 .mustache 
		// (application에 지정하지 않아도 기본 이 설정임.
		return "index"; // src/main/resources/templates/index.mustache 
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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
	

}
