package com.idea.recon.controllers;

import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.SchoolDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.services.SchoolService;

@CrossOrigin
@RestController
@RequestMapping(value = "/school")
public class SchoolController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SchoolService schoolService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	//MAKE THIS PAGINATION
	@GetMapping(value = "/{id}/students")
	@PreAuthorize("hasAuthority('school')")
	ResponseEntity<Set<TraineeDTO>> getMyStudents(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(schoolService.getStudents(id, token), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('school')")
	ResponseEntity<SchoolDTO> getMyDetails(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(schoolService.getMyDetails(id, token), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('school')")
	ResponseEntity<String> updateMyDetails(@RequestBody SchoolDTO updateInfo, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(schoolService.updateMyDetails(updateInfo, token), HttpStatus.OK);
	}
	
	@PostMapping(value = "/{id}/add-student")
	@PreAuthorize("hasAuthority('school')")
	ResponseEntity<String> addStudentToRoster(@PathVariable Integer id, @RequestParam(value = "sid") String studentEmail, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		Thread.sleep(10000);
		return new ResponseEntity<>(schoolService.RegisterStudent(id, studentEmail, token), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/{id}/add-contractor")
	@PreAuthorize("hasAuthority('school')")
	ResponseEntity<String> addContractorToRoster(@PathVariable Integer id, @RequestParam(value = "cid") String contractorEmail, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		Thread.sleep(10000);
		return new ResponseEntity<>(schoolService.RegisterContractor(id, contractorEmail, token), HttpStatus.CREATED);
	}
	
	

}
