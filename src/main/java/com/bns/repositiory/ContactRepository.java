package com.bns.repositiory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bns.entites.Contact;
import com.bns.entites.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// pagination
	// public List<Contact> findContactsByUser(Integer userId);

//	@Query(value = "from Contact as contact where contact.user.userId=:userId")
//	public List<Contact> findContactsByUserId(@Param("userId") Integer userId);

	@Query(value = "from Contact as contact where contact.user.userId=:userId")
	public Page<Contact> findContactsByUserId(@Param("userId") Integer userId, Pageable pageable);

	//search contact
	
	//public List<Contact> findByFirstNameOrLastNameContainingAndUser(String firstName,String lastName,User user);

	public List<Contact> findByFirstNameContainingAndUser(String name, User user);

	public List<Contact> findByFirstNameContainingOrLastNameContainingAndUser(String firstName, String lastName,
			User user);
	
}
