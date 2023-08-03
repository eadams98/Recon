package com.idea.recon.controllers;

import java.util.Set;

import javax.ws.rs.QueryParam;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.services.ConnectionService;
import com.idea.recon.services.ContractorService;

@CrossOrigin
@RestController
@RequestMapping(value = "/contractor")
public class ContractorController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ContractorService contractorService;
	
	@Autowired
	ConnectionService connectionService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@GetMapping(value = "/{id}/trainees")
	//@PreAuthorize("hasRole('trainee')")
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@PreAuthorize("hasAuthority('contractor')")
	Set<TraineeDTO> getContractorTrainees(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) throws Exception {
		logger.info("GET TRAINEES");
		token = token.split(" ")[1];
		logger.info("token: " + token);
		logger.info("username from claims on token: " + jwtTokenUtil.getUsernameFromToken(token).toString());
		return contractorService.getContractorsTrainees(id, token);
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('contractor')")
	ResponseEntity<ContractorDTO> getMyDetails(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(contractorService.getMyDetails(id, token), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('contractor')")
	ResponseEntity<String> updateMyDetails(@RequestBody ContractorDTO updateInfo, @RequestHeader (name="Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(contractorService.updateMyDetails(updateInfo, token), HttpStatus.OK);
	}
	
	// HELPERS
	@GetMapping(value = "/verify")
	@PreAuthorize("hasAuthority('contractor')")
	ResponseEntity<RelationshipVerificationDTO> verifyContractorTraineeRelationship(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestHeader (name="Authorization") String token) throws Exception {
		logger.info("by email: " + byEmail + ", for email: " + forEmail);
		token = token.split(" ")[1];
		return new ResponseEntity<>(contractorService.confirmContractorTraineeConnection(byEmail, forEmail, token), HttpStatus.OK);
	}
	
	@GetMapping(value = "/confirm-access-requst/{id}")
	@PreAuthorize("hasAuthority('contractor')")
	ResponseEntity<Boolean> getContractorId(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) throws ContractorException, Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(contractorService.confirmAccessRequest(id, token), HttpStatus.OK);
	}
	
	@GetMapping(value = "/unregistered-to-school")
	ResponseEntity<Set<ContractorDTO>> getContractorsNotAssignedToThisSchool(@RequestParam(value = "se") String schoolEmail) throws ContractorException, Exception {
		return new ResponseEntity<>(connectionService.getContractorsNotRegisteredToThisSchool(schoolEmail), HttpStatus.OK);
	}

}
