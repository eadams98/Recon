package com.idea.recon.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.services.VerificationService;

@CrossOrigin
@Controller
@RequestMapping("/verify")
public class VerificationController {
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	VerificationService verificationService;
	
	@GetMapping(value = "/contractor-to-trainee")
	@PreAuthorize("hasAnyAuthority('trainee', 'contractor')")
	ResponseEntity<RelationshipVerificationDTO> verifyContractorTraineeRelationship(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestHeader (name="Authorization") String token) throws Exception {
		logger.info("by email: " + byEmail + ", for email: " + forEmail);
		token = token.split(" ")[1];
		return new ResponseEntity<>(verificationService.route(byEmail, forEmail, token), HttpStatus.OK);
	}
}
