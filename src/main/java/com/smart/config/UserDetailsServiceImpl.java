package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userrepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userrepository.getUserByName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user"); 
		}
		UserDetail userfg=new UserDetail(user);
		return userfg;
	}

}
