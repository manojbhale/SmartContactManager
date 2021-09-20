package com.bns.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bns.entites.User;

@Repository
public interface UserRepositiory extends JpaRepository<User, Integer>{

	
	//public User findByEmail(String email);  Derived query
	
	@Query(value = "select user from User user where user.email = :email")
	public User getUsernameByEmail(@Param("email") String email);
	
	
}

