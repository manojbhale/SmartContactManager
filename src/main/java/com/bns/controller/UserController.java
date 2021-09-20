package com.bns.controller;

import java.security.Principal;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bns.entites.User;
import com.bns.helper.MessageHelper;
import com.bns.service.EmailService;
import com.bns.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/do_register")
	@Transactional
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") Boolean agreement, Model model,
			HttpSession session) {

		System.out.println("Agreement " + agreement);
		System.out.println("user " + user);

		try {
			if (!agreement) {
				System.out.println("User did not agreed terms & conditions");
				throw new Exception("User did not agreed terms & conditions");

			}
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setCreatedBy(user.getFirstName() + " " + user.getLastName());
			User resultUser = userService.saveUser(user);
			model.addAttribute("resultUser", resultUser);
			session.setAttribute("message", new MessageHelper("Successfully Registered ", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message",
					new MessageHelper("Something Went Wrong " + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	// get My-Profile handler

	@GetMapping("/user/my-profile")
	public String getMyProfile(Model model, Principal principal) {

		String userName = principal.getName();
		User user = userService.getUserByUserName(userName);

		model.addAttribute("title", "SCM | My-Profile");
		model.addAttribute("user", user);

		return "normal/my-profile";
	}

	// open setting handler
	@GetMapping("/user/setting")
	public String getSetting() {
		return "normal/setting";
	}

	// handle chnage password process
	@PostMapping("/user/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {

		System.out.println("Old Password " + oldPassword);
		System.out.println("New Password " + newPassword);

		String userName = principal.getName();

		User user = userService.getUserByUserName(userName);

		if (this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			// set new password

			user.setPassword(passwordEncoder.encode(newPassword));
			this.userService.saveUser(user);
			session.setAttribute("message", new MessageHelper("Password has been Changed", "success"));

		} else {
			// error
			System.out.println("please fill your correct old password" + oldPassword);
			session.setAttribute("message", new MessageHelper("please fill your correct old password", "danger"));
			return "redirect:/user/setting"; // for url return
		}

		// return "normal/user_dashboard"; //for view Return
		return "redirect:/user/index"; // for url return
	}

	// open forgot password

	@GetMapping("/forgotPassowrd")
	public String forgotPasswordLink() {
		return "forgotEmail_form";
	}

	// send otp
	@PostMapping("/sendOtp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) {
		System.out.println("Email : " + email);

		// genrate random number

		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000);
		String otp = String.format("%05d", num);
		System.out.println(otp);

		String subject = "OTP From Smart Contact Manager";
		String message = "<div style='border:1px solid #e2e2e2; padding:20px;'>" + "<h2>" + "OTP &nbsp;&nbsp;" + "<b>"
				+ otp + "</b>" + "</h2>" + "</div>";
		boolean flag = emailService.sendEmail(subject, message, email);

		if (flag) {
			session.setAttribute("myOtp", otp);// store otp in session or you can store in db
			session.setAttribute("email", email);
			return "verify_otp";
		} else {
			session.setAttribute("message", "Please check your Email Id");
			return "forgotEmail_form";
		}

	}

	// verifyOtp

	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp") String otp, HttpSession session) {

		System.out.println("Otp : " + Integer.parseInt(otp));

		String myOTP = (String) session.getAttribute("myOtp");
		int myOtp = Integer.parseInt(myOTP);
		String email = (String) session.getAttribute("email");

		if (myOtp == Integer.parseInt(otp)) {
			// password change form

			User user = userService.getUserByUserName(email);
			if (user == null) {
				// send error message
				session.setAttribute("message", "user doesn't with this Email Id ");
				return "forgotEmail_form";

			} else {
				// send change password form
				return "password_changeForm";
			}

		} else {
			session.setAttribute("message", "You have entered wrong OTP  !!");
			return "verify_otp";
		}

	}

	// changePassword
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session) {

		String email = (String) session.getAttribute("email");

		User user = userService.getUserByUserName(email);
		user.setPassword(passwordEncoder.encode(newPassword));
		userService.saveUser(user);
		
		return "redirect:/signin?changePwd=your password has changed successfully";
	}

}
