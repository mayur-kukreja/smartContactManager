package com.smart.controller;

import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private ContactRepository contactrepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();

		User user = userrepository.getUserByName(username);

		model.addAttribute("user", user);

	}

	@GetMapping("/add-contact")
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("userimage") MultipartFile file,
			Principal principal, HttpSession session)

	{
		try {
			String name = principal.getName();
			User user = this.userrepository.getUserByName(name);

			contact.setUser(user);

			if (file.isEmpty()) {
				contact.setImage("contact.png");

			} else {
				contact.setImage(file.getOriginalFilename());
				java.io.File savefile = new ClassPathResource("static/image").getFile();
				java.nio.file.Path path = Paths
						.get(savefile.getAbsolutePath() + java.io.File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}

			user.getContacts().add(contact);

			this.userrepository.save(user);

			

			session.setAttribute("message", new Message("Contact Added Sucessfully", "sucess"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong try again", "danger"));
		}
		return "normal/add_contact";

	}

	@GetMapping("/show-contact")
	public String showContact(Model model, Principal principal) {
		model.addAttribute("title", "Show user contacts");
		String username = principal.getName();
		User user = this.userrepository.getUserByName(username);

		List<Contact> contact = this.contactrepository.findContactByUser(user.getId());

		model.addAttribute("contact", contact);

		return "normal/show_contact";
	}

	@RequestMapping("/contact_detail/{cid}")
	public String getContactDetails(@PathVariable("cid") int cid, Model model, Principal principal) {
		Optional<Contact> optionalcontact = this.contactrepository.findById(cid);

		Contact contact = optionalcontact.get();
		String username = principal.getName();
		User user = this.userrepository.getUserByName(username);
		if (user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);
		model.addAttribute("title", user.getName());

		return "normal/contact_detail";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, Model model, Principal principal) {
		
		Optional<Contact> optionalcontact = this.contactrepository.findById(cid);
		Contact contact = optionalcontact.get();

		User user = this.userrepository.getUserByName(principal.getName());
		user.getContacts().remove(contact);
		this.userrepository.save(user);

		return "redirect:/user/show-contact";

	}

	@PostMapping("/update-form/{cid}")
	public String updateContact(Model model, @PathVariable("cid") int cid) {
		model.addAttribute("title", "update form");

		Contact contact = this.contactrepository.findById(cid).get();

		model.addAttribute("contact", contact);
		return "normal/update_form";

	}

	@PostMapping("/process-update")
	public String processUpdate(@ModelAttribute Contact contact, @RequestParam("userimage") MultipartFile file, Model m,
			HttpSession session, Principal p) {
		try {
			if (!file.isEmpty()) {
				contact.setImage(file.getOriginalFilename());
				java.io.File savefile = new ClassPathResource("static/image").getFile();
				java.nio.file.Path path = Paths
						.get(savefile.getAbsolutePath() + java.io.File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is added");

			}

			User user = this.userrepository.getUserByName(p.getName());
			contact.setUser(user);
			this.contactrepository.save(contact);
			m.addAttribute("message", new Message("your contact is updated", "sucess"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "normal/contact_detail";
	}

	@GetMapping("/profile")
	public String viewProfile(Model model) {
		model.addAttribute("title", "Personal Dashboard");
		return "normal/dashboard";
	}

	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}

	@GetMapping("/homepage")
	public String homePage() {
		return "normal/userhome";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword") String oldPassword,
			@RequestParam("newpassword") String newpassword, Principal principal, Model model) {
		
		String userName = principal.getName();
		User user = this.userrepository.getUserByName(userName);
		if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userrepository.save(user);
			model.addAttribute("message", new Message("Password change sucessfully", "sucess"));
		} else {
			model.addAttribute("message", new Message("Password not change", "danger"));
			return "redirect:/user/settings";

		}

		return "redirect:/user/homepage";
	}
}
