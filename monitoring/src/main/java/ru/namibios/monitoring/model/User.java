package ru.namibios.monitoring.model;

import org.springframework.security.core.context.SecurityContextHolder;

public class User {
	
	private String name;
	
	public User() {
		this.name = SecurityContextHolder.getContext().getAuthentication().getName();
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
	
}
