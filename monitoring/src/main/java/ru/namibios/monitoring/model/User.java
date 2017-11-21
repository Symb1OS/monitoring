package ru.namibios.monitoring.model;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class User {
	
	public static final String ROLE_USER = "ROLE_USER";
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	private String name;
	
	private Collection<? extends GrantedAuthority> authority;
	
	public User() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		this.name = auth.getName();
		this.authority = auth.getAuthorities();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHash() {
		return ShaEncoder.encode(name);
	}

	public Collection<? extends GrantedAuthority> getAuthority() {
		return authority;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", authority=" + authority + "]";
	}
	
}
