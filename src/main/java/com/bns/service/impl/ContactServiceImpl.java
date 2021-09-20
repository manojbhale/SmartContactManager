package com.bns.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bns.entites.Contact;
import com.bns.entites.User;
import com.bns.repositiory.ContactRepository;
import com.bns.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Contact saveContact(Contact contact) {
		return contactRepository.save(contact);
	}

	@Override
	public Page<Contact> findContactsByUserId(Integer userId, Integer page) {
		Pageable pageable = PageRequest.of(page, 7);

		return contactRepository.findContactsByUserId(userId, pageable);
	}

	@Override
	public Contact getContactById(Integer contactId) {
		Optional<Contact> opContact = contactRepository.findById(contactId);
		Contact contact = null;
		if (opContact.isPresent()) {
			contact = opContact.get();
		}
		return contact;
	}

	@Override
	public void deleteContact(Contact contact) {
		contactRepository.delete(contact);
	}

	@Override
	public List<Contact> searchContact(String name, User user) {

		return contactRepository.findByFirstNameContainingAndUser(name,user);
	}

	@Override
	public List<Contact> searchContact(String firstName,String lastName,User user) {
		return contactRepository.findByFirstNameContainingOrLastNameContainingAndUser(firstName,lastName, user);
	}

	
	
	
}
