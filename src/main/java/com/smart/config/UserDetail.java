package com.smart.config;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smart.entities.User;

public class UserDetail implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public UserDetail(User user) {
		super();
		this.user = user;
	}


	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simplegrantedauthority=new SimpleGrantedAuthority(user.getRole());
		List<GrantedAuthority> list=new ArrayList();
		list.add(simplegrantedauthority);
		
		
		return list;
	}

	
	public String getPassword() {
		
		return user.getPassword();
	}

	
	public String getUsername() {
		
		return user.getEmail();
	}

	
	public boolean isAccountNonExpired() {
		
		return true;
	}

	
	public boolean isAccountNonLocked() {
		
		return true;
	}

	
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	
	public boolean isEnabled() {
		return true;
	}

}
