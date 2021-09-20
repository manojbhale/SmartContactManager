package com.bns.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.bns.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	String from = "manojbhale.test@gmail.com";

	@Override
	public boolean sendEmail(String subject, String message, String to) {
		// rest of the code

		boolean flag = false;

		// variable for gmail
		String host = "smtp.gmail.com";
		// get the System properties

		Properties properties = System.getProperties();
		System.out.println("System Properties : " + properties);

		// Setting important information to properties object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// step:1 to get the session object

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("manojbhale.test@gmail.com", "M@n0jbh@!etest");
			}

		});
		session.setDebug(true); // print information on console
		// step: 2 compose the message[text,attachement,multimedia]

		MimeMessage mimeMessage = new MimeMessage(session);

		// from Email

		try {
			mimeMessage.setFrom(from);

			// adding recipient to message

			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject

			mimeMessage.setSubject(subject);

			// adding text to message

		//	mimeMessage.setText(message);
			
			mimeMessage.setContent(message, "text/html");//for send html content on email
			

			// step 3 : send Message using Transport class

			Transport.send(mimeMessage);

			System.out.println("Send Email Successfully");
			flag = true;

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
