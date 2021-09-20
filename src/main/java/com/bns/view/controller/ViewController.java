package com.bns.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bns.entites.User;

@Controller
public class ViewController {

//	@Autowired
//	private UserService userService;

	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "Working Fine!!!";
	}

	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "SCM|Login");
		return "login";
	}

	@GetMapping("/login-fail")
	public String loginFail(Model model) {
		model.addAttribute("title", "SCM|Login Fail");
		return "login-fail";
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "SCM|Home");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "SCM|About");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "SCM|Register");
		model.addAttribute("user", new User());
		return "signup";
	}

//	@GetMapping("/user/addContact")
//	public String addContactForm(Model model) {
//		model.addAttribute("title", "SCM| Add-Contact");
//		model.addAttribute("contact", new Contact());
//		return "normal/add_contact";
//	}
//
//	@GetMapping("/user/index")
//	public String dashboard(Model model) {
//		model.addAttribute("title", "SCM|Dashboard");
//		return "normal/user_dashboard";
//	}

	// show contacts handler
//
//	@GetMapping("/user/showContacts")
//	public String showContacts(Model model, Principal principal) {
//	//	model.addAttribute("title", "SCM| Show-Contacts");
//		// also send contacts list from this side
//
////		String userName = principal.getName();
////		User user = userService.getUserByUserName(userName);
////		List<Contact> constacts = user.getConstacts();
////		model.addAttribute("contacts", constacts);
//
//		return "/showContacts/{page}";
//	}

}
