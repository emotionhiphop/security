 package com.ktj.security1.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ktj.security1.model.User;

//security가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다. 
//로그인이 완료가 되면 시큐리티 session이 만들어 진다. (Security ContextHolder)
//Object 타입 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 된다. 
//User오브젝트 타입 => UserDetails 타입

//Security Session => Authentication => UserDetails(PricipalDetails)

public class PrincipalDetails implements UserDetails {
	
	private User user;//컴포지션
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//해당 유저의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collect = new ArrayList<>();
		
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return user.getRole();
			}
		});
		
		return collect;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 예 > 일년동안 로그인이 안되면 휴면계정으로 .. false 처리..
		return true;
	}

	
}
