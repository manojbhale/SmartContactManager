package com.bns.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bns.entites.Contact;
import com.bns.entites.User;
import com.bns.helper.MessageHelper;
import com.bns.service.ContactService;
import com.bns.service.UserService;

@Controller
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private UserService userService;

	
	@GetMapping("/demoAjax1")
	public String ajaxCallDemo1() {
		return "DemoAjax1";
	}
	
	@GetMapping("/demo")
	public String demoFile() {
		return "Demo";
	}

	
	@PostMapping("/addContact")
	public Contact saveContact(Contact contact) {
		return contactService.saveContact(contact);
	}

	// processing add contact

	@PostMapping("/processContact")
	public String processContact(@ModelAttribute @Valid Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {

		try {

			String userName = principal.getName();
			User user = userService.getUserByUserName(userName);

			// processing and uploading file

			// 1 . check file is empty or not

			if (file.isEmpty()) {
				System.out.println("File is Empty");
				contact.setProfileImage("contact.png");

			} else {
				// upload file to the folder and update the name in contact
				contact.setProfileImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}

			contact.setUser(user);
			contact.setCreatedBy(user.getFirstName() + " " + user.getLastName());
			this.saveContact(contact);

			// message success print here

			session.setAttribute("message", new MessageHelper("Your Contact is Added !! Add More", "success"));

		} catch (Exception e) {
			// System.out.println("ERROR :" + e.getMessage());
			e.printStackTrace();

			// message error print here
			session.setAttribute("message", new MessageHelper("Something went Wrong !! Try Again!", "danger"));

		}

		return "/normal/add_Contact";
	}

	// processing view Contacts

	// pagination concept
	// per page record = 5[one page contact]
	// current page = 0 replcae [page]
	//

//
//	@GetMapping("/showContacts/{page}")
//	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
//		model.addAttribute("title", "SCM | Show-Contacts");
//		String userName = principal.getName();
//		User user = userService.getUserByUserName(userName);
//
//		Page<Contact> contacts = contactService.findContactsByUserId(user.getUserId(), page);
//		model.addAttribute("contacts", contacts);
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPages", contacts.getTotalPages());
//
//		return "/normal/showContacts";
//	}

	@GetMapping("/user/showContacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "SCM | Show-Contacts");
		String userName = principal.getName();
		User user = userService.getUserByUserName(userName);

		Page<Contact> contacts = contactService.findContactsByUserId(user.getUserId(), page);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "/normal/showContacts";
	}

	@GetMapping("/user/addContact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "SCM| Add-Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

	@GetMapping("/user/index")
	public String dashboard(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userService.getUserByUserName(userName);
		model.addAttribute("title", "SCM|Dashboard");
		model.addAttribute("user", user);
		return "normal/user_dashboard";
	}

	// showing particular contact handler

	@GetMapping("/user/{contactId}/contact")
	public String showParticularContact(@PathVariable("contactId") Integer contactId, Model model,
			Principal principal) {
		System.out.println("Contact Id: " + contactId);

		Contact contact = contactService.getContactById(contactId);

		// check conditon
		String loggedInUser = principal.getName();
		User user = userService.getUserByUserName(loggedInUser);

		if (user.getUserId() == contact.getUser().getUserId()) {

//			model.addAttribute("title", "SCM | Contact");
			model.addAttribute("title", "SCM | " + contact.getFirstName() + ' ' + contact.getLastName());
			model.addAttribute("contact", contact);
		}
		return "normal/contact_detail";
	}

	// delete contact handler

	@GetMapping("/user/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") Integer contactId, Model model, Principal principal,
			HttpSession session) {

		Contact contact = contactService.getContactById(contactId);

		String userName = principal.getName();

		User user = userService.getUserByUserName(userName);
		// who will delete this contact of owner
		if (user.getUserId() == contact.getUser().getUserId()) {
			contact.setUpdatedBy(user.getFirstName()+' '+user.getLastName());
			contactService.deleteContact(contact);
			session.setAttribute("message", new MessageHelper("Contact deleted successfully !!", "success"));

		}

		return "redirect:/user/showContacts/0";
	}

	// update for form open contact

	@PostMapping("/user/update-contact/{contactId}")
	public String updateContact(@PathVariable("contactId") Integer contactId, Model model) {

		Contact contact = contactService.getContactById(contactId);

		model.addAttribute("title", "SCM | Update-Contact");
		model.addAttribute("contact", contact);
		return "normal/update_contact";
	}

	// /user/processUpdateContact

	@PostMapping("/user/processUpdateContact")
	public String updateProcessContact(@ModelAttribute @Valid Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile file, Model model, HttpSession session, Principal principal) {

		System.out.println("Contact " + contact.getFirstName());

		try {

			// get old contact details

			Contact oldContact = contactService.getContactById(contact.getContactId());

			if (!file.isEmpty()) {

				//delete oldfile
				
				File deleteFile = new ClassPathResource("static/images").getFile();
				
				File isFile = new File(deleteFile, oldContact.getProfileImage());
				isFile.delete();
				
				
				//update new photo
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setProfileImage(file.getOriginalFilename());
			} else {
				contact.setProfileImage(oldContact.getProfileImage());
			}
			String userName = principal.getName();
			User user = userService.getUserByUserName(userName);
			contact.setUpdatedBy(user.getFirstName() + ' ' + user.getLastName());
			contact.setUpdatedDate(new Date());
			contact.setUser(user);
			contact.setCreatedBy(oldContact.getCreatedBy());
			contact.setCreatedDate(oldContact.getCreatedDate());
			Contact saveContact = contactService.saveContact(contact);

			session.setAttribute("message", new MessageHelper("Your contact has been modified!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}
//user/6/contact
		return "redirect:/user/" + contact.getContactId() + "/contact";
	}
	
	
	
	
	
	
	
	

}
