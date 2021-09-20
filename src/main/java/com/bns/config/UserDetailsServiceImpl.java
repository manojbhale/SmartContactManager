package com.bns.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bns.entites.User;
import com.bns.repositiory.UserRepositiory;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepositiory userRepositiory;
	
	private CustomUserDetailsImpl customUserDetailsImpl =null;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//Fetch User from Database
		User user = userRepositiory.getUsernameByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not found User");
		}
		customUserDetailsImpl = new CustomUserDetailsImpl(user);
		
		return customUserDetailsImpl;
	}

}
