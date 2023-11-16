package com.inn.cafe.utils;

import java.util.List;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendSimpleMessage(String to, String text, String subject, List<String> emailList) {
		SimpleMailMessage message= new SimpleMailMessage();
		message.setFrom("livelifelikebee@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if(Objects.nonNull(emailList)&& !emailList.isEmpty()) {
			message.setCc(getCcArray(emailList));
		}
		
		javaMailSender.send(message);
		
	}

	private String[] getCcArray(List<String> ccList) {
		String[] cc= new String[ccList.size()];
		for(int i=0; i<ccList.size(); i++) {
			cc[i]= ccList.get(i);
		}
		return cc;
	}
	
	public void forgotMail(String to, String subject, String password) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(message, true);
		helper.setFrom("livelifelikebee@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		message.setContent(htmlMsg,"text/html");
		javaMailSender.send(message);
		
		
	}
}
