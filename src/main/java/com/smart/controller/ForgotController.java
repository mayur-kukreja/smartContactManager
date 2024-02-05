package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.service.EmailSender;

@Controller
public class ForgotController {
	Random random = new Random(1000);
	
	@Autowired
	EmailSender emailsender;

	@RequestMapping("/forgot")
	public String openForgotForm() {
		return "forgot_form";

	}

	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email) {
		System.out.println(email);

		int otp = random.nextInt(99999999);
		System.out.println(otp);
		String subject="OTP from SCM";
		String message="<h1> OTP"+otp+"</h1>";
		this.emailsender.sendEmail(email,subject ,message);
		return "verify_otp";

	}

}
