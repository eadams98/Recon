package com.idea.recon.services;

import java.util.Set;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.SchoolException;

public interface ConnectionService { 

	public Set<ContractorDTO> getContractorsNotRegisteredToThisSchool(String schoolEmail) throws ContractorException, SchoolException;
	
}
