package com.ktj.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //spring security filter가 spring filter chain에 등록이 된다.
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
			.antMatchers("/manager/**").hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER")
			.antMatchers("/admin/**").hasRole("ROLE_ADMIN")
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
