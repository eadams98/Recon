package com.idea.recon.services;

import java.util.List;
import java.util.Set;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.TraineeException;

public interface ContractorService {

	Set<TraineeDTO> getContractorsTrainees(Integer id, String token) throws ContractorException;
	ContractorDTO getMyDetails(Integer id, String token) throws ContractorException;
	String updateMyDetails(ContractorDTO updateInfo, String token) throws ContractorException;
	
	// called by other services
	RelationshipVerificationDTO confirmContractorTraineeConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException;
	Boolean confirmAccessRequest(Integer id, String token) throws ContractorException, Exception; 
}
