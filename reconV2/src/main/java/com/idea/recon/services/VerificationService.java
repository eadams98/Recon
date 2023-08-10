package com.idea.recon.services;

import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.TraineeException;

public interface VerificationService {

	public RelationshipVerificationDTO route(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException;
	//RelationshipVerificationDTO confirmContractorToTraineeConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException;
	//RelationshipVerificationDTO confirmTraineeToContractorConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException;
	
}
