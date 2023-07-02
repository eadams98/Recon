package com.idea.recon.controllers;

import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.exceptions.TraineeException;
import com.idea.recon.services.TraineeService;

@CrossOrigin
@Controller
@RequestMapping("/trainee")
public class TraineeController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TraineeService traineeService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@GetMapping(value = "/{id}/contractor")
	@PreAuthorize("hasAuthority('trainee')")
	ResponseEntity<ContractorDTO> getSupervisor(@PathVariable Integer id, @RequestHeader(name="Authorization") String token) throws TraineeException {
		token = token.split(" ")[1];
		return new ResponseEntity<>(traineeService.getSupervisor(id, token), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('trainee')")
	ResponseEntity<TraineeDTO> getMyDetails(@PathVariable Integer id, @RequestHeader(name="Authorization") String token) throws TraineeException {
		token = token.split(" ")[1];
		return new ResponseEntity<>(traineeService.getMyDetails(id, token), HttpStatus.OK);
	}
	
	// Pagination needed so as not to have MILLIONS
	// can only be accessed by school due to JwtRequestFilter. Must have a school token 
	@GetMapping(value = "/unregistered")
	ResponseEntity<Set<TraineeDTO>> getUnregisteredTrainees() throws TraineeException {
		//token = token.split(" ")[1];
		return new ResponseEntity<>(traineeService.getTraineesNotRegisteredToSchool(), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('trainee')")
	ResponseEntity<String> updateMyDetails(@RequestBody TraineeDTO updateInfo, @RequestHeader(name="Authorization") String token) throws TraineeException {
		token = token.split(" ")[1];
		return new ResponseEntity<>(traineeService.updateMyDetails(updateInfo, token), HttpStatus.OK);
	}

}
