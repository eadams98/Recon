package com.idea.recon.services;

import java.util.Set;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.TraineeException;

public interface TraineeService {
	
	ContractorDTO getSupervisor(Integer id, String token) throws TraineeException;
	TraineeDTO getMyDetails(Integer id, String token) throws TraineeException;
	String updateMyDetails(TraineeDTO updateInfo, String token) throws TraineeException;
	
	Trainee getTraineeByEmail(String email) throws TraineeException;
	Set<TraineeDTO> getTraineesNotRegisteredToSchool() throws TraineeException;

}
