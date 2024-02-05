package com.smart.controller;

import javax.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	UserRepository userrepository;
	
	@Autowired
	PasswordEncoder passwordencoder;

	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/signup/")
	public String about(Model model) {
		model.addAttribute("user", new User());
		return "signup";

	}

	@PostMapping("/do_register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean enabled, Model model,
			HttpSession session) {
		

		try
		{
			if (!enabled) {
				System.out.println("You have not agred terms and condition");
				throw new Exception("You have not agreed terms and condition");
			}
			if(result.hasErrors())
			{
				model.addAttribute("user",user);
				System.out.println("Error"+result.toString());
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordencoder.encode(user.getPassword()));
			System.out.println(user);
			System.out.println(enabled);

			User result1=this.userrepository.save(user);

			model.addAttribute("user",new User());
			
			session.setAttribute("message",new Message("Sucessfully Registered","alert-sucess"));
			
			System.out.println(result);
			
			
			return "signup";
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message",new Message("Something went wrong "+e.getMessage(),"alert-danger"));
			return "signup";
			
		}

		
	}
	
	@GetMapping("/signin")
	String coustomLogin(Model model)
	{
		model.addAttribute("title","loginPage");
		
		return "login";
	}
	
	@RequestMapping(value="/login-fail")
	String login(HttpSession session)
	{
		session.setAttribute("message",new Message("Invalid username or password","alert-danger"));
		
		
		return "login";
	}
	
	
	
	
	

}
