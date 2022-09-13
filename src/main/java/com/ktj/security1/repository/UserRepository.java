package com.ktj.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ktj.security1.model.User;

//CRUD 함수를 JpaRepository가 들고 있다. 
// @Repository를 선언하지 않아도 JpaRepository가 있어서 IOC가 된다.  
public interface UserRepository extends JpaRepository<User, Integer> {

	//select * from user where username = ? 
	// findBy 규칙
	public User findByUsername(String username); //JPA Query Methods

}
