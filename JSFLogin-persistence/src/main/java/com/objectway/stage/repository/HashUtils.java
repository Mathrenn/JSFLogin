package com.objectway.stage.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashUtils {
	public static String hash(String pass) {
		PasswordEncoder pe = new BCryptPasswordEncoder();
		return pe.encode(pass);
	}
	
	public static boolean matches(String pass, String hash) {
		PasswordEncoder pe = new BCryptPasswordEncoder();
		return pe.matches(pass, hash);
	}
}
