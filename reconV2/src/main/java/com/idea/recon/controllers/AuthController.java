package com.idea.recon.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dtos.UserLoginDTO;
import com.idea.recon.repositories.RoleRepository;
import com.idea.recon.repositories.TraineeLoginRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	/*
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TraineeLoginRepository traineeLoginRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/hello")
	public String helloWorld() {
		logger.info("HIT API");
		return "Hello World";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginDTO loginDTO) {
		logger.info("HIT API");
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDTO.getEmail(),
						loginDTO.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new ResponseEntity<>("User Sign in Success", HttpStatus.OK);
	}*/
 
}
