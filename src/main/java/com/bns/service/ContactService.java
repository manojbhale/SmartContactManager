package com.bns.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bns.entites.Contact;
import com.bns.entites.User;

public interface ContactService {

	public Contact saveContact(Contact contact);

	public Page<Contact> findContactsByUserId(Integer userId, Integer page);

	public Contact getContactById(Integer contactId);

	public void deleteContact(Contact contact);

	public List<Contact> searchContact(String name, User user);

	public List<Contact> searchContact(String firstName, String lastName, User user);
}
