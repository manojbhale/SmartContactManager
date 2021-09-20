package com.bns.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bns.entites.User;
import com.bns.repositiory.UserRepositiory;
import com.bns.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepositiory userRepositiory;

	@Override
	public User saveUser(User user) {

		String about = user.getAbout().trim();
		user.setAbout(about);
		return userRepositiory.save(user);
	}

	@Override
	public User getUserByUserName(String userName) {		
		return userRepositiory.getUsernameByEmail(userName);
	}

}
