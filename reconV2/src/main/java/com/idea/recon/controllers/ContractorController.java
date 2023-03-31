package com.idea.recon.controllers;

import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Trainee;
import com.idea.recon.services.ContractorService;

@CrossOrigin
@RestController
@RequestMapping(value = "/contractor")
public class ContractorController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ContractorService contractorService;
	
	@GetMapping(value = "/{id}")
	//@PreAuthorize("hasRole('trainee')")
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@PreAuthorize("hasAuthority('trainee')")
	Set<TraineeDTO> getContractorTrainees(@PathVariable Integer id) throws Exception {
		logger.info("GET TRAINEES");
		return contractorService.getContractorsTrainees(id);
	}

}
