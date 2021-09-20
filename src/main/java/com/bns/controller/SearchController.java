package com.bns.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bns.entites.Contact;
import com.bns.entites.User;
import com.bns.service.ContactService;
import com.bns.service.UserService;

@RestController
public class SearchController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	// search Handler
	@GetMapping("/user/search/{query}")
	public ResponseEntity<?> searchContact(@PathVariable("query") String name, Principal principal) {

		String firstName = name;
		String lastName = name;

		String userName = principal.getName();
		User user = userService.getUserByUserName(userName);
		List<Contact> searchContact = contactService.searchContact(firstName, lastName, user);

		// List<Contact> searchContact = contactService.searchContact(name,user);

		return ResponseEntity.ok(searchContact);
	}

}
