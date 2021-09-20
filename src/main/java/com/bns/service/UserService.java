package com.bns.service;

import com.bns.entites.User;

public interface UserService {

	User saveUser(User user);

	User getUserByUserName(String userName);
	

}
