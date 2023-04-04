package com.idea.recon.services;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.exceptions.TraineeException;

public interface TraineeService {
	
	ContractorDTO getSupervisor(Integer id, String token) throws TraineeException;
	TraineeDTO getMyDetails(Integer id, String token) throws TraineeException;
	String updateMyDetails(TraineeDTO updateInfo, String token) throws TraineeException;

}
