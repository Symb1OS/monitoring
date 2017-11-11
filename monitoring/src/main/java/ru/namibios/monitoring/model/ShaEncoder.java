package ru.namibios.monitoring.model;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class ShaEncoder {
	
	private static final String SALT = "youwillnotpass";
	
	public static String encode(String pwd) {
		 
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		return passwordEncoder.encodePassword(pwd, SALT);
		
	}
	
}