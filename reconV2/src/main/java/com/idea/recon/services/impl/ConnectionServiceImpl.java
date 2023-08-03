package com.idea.recon.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.entities.School;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.SchoolException;
import com.idea.recon.repositories.SchoolToContractorRepository;
import com.idea.recon.services.ConnectionService;
import com.idea.recon.services.SchoolService;

@Service
public class ConnectionServiceImpl implements ConnectionService {
	
	@Autowired
	SchoolService schoolService;
	
	@Autowired 
	SchoolToContractorRepository schoolToContractorRepository;

	@Override
	public Set<ContractorDTO> getContractorsNotRegisteredToThisSchool(String schoolEmail) throws ContractorException, SchoolException {
		
		School school = schoolService.getSchoolByEmail(schoolEmail);
		Set<ContractorDTO> contractorsNotAttachedToThisSchool = new HashSet<>();
		schoolToContractorRepository.getContractorsNotAssignedToSchool(school).forEach(c -> {
			contractorsNotAttachedToThisSchool.add( ContractorDTO.builder()
				.firstName(c.getFirstName())
				.lastName(c.getLastName())
				.email(c.getEmail())
				//.id(null)
				.build()
			);
		});
		return contractorsNotAttachedToThisSchool;
	}

}
