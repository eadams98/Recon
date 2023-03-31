package com.idea.recon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dtos.UserLoginDTO;
import com.idea.recon.services.TraineeLoginService;

@CrossOrigin
@RestController
@RequestMapping(value = "/Login")
public class LoginController {
	
	@Autowired
	TraineeLoginService traineeLoginService;
	
	@GetMapping(value = "/attempt/{id}/{password}") 
	String attemptLogin(@PathVariable Long id, @PathVariable String password) {
		return traineeLoginService.validateUserLogin(id, password);
	}

}
