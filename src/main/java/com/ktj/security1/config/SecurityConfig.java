package com.ktj.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //spring security filter가 spring filter chain에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화 
public class SecurityConfig extends WebSecurityConfigurerAdapter{//-->spring security filter

	//해당 메소드의 리턴되는 오브젝트를 ioc로 등록해준다. 
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
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
			.defaultSuccessUrl("/");
		
			
			//access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			//.anyRequests().permitAll();
			//.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			

			
	}
}
