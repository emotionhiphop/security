package com.ktj.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ktj.security1.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity //spring security filter가 spring filter chain에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화 
public class SecurityConfig extends WebSecurityConfigurerAdapter{//-->spring security filter

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService; 
	//해당 메소드의 리턴되는 오브젝트를 ioc로 등록해준다. 
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	
	//1.코드받기(인증) 2. 엑세스토큰(권한)
	//3. 사용자프로필 정보를 가져오고 4-1. 그정보를 토대로 회원가입을 자동으로 진행시키거나
	//4-2. (이메일,전화번호, 이름, 아이디) 외에 추가 정보를 입력받고 회원 가입을 시키거나..
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeHttpRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") //ROLE_ 제외시켜야 정상작동 (db는 ROLE_ 붙히고..
			.antMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll()//나머지 경로는 모두 허용
			.and()
			.formLogin()
			.loginPage("/loginForm") //로긴이 안되었다면 /login으로 이동하라.
			.loginProcessingUrl("/login") //login 주소가 호출되면 security가 낚아채서 대신 로그인을 진행해준다. 
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login() ///oauth2/authorization/google을 처리하게 해줌..
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService);// 구글 로그인이 완료된 뒤에 후처리가 필요함. Tip. 코드를 받는 것이 아니라  (엑세스토큰 + 사용자프로필정보) 한번에 받는다 oauth2-client 역할
		
		
			
			//access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			//.anyRequests().permitAll();
			//.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			

			
	}
}
