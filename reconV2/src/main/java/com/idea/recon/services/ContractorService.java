package com.idea.recon.services;

import java.util.List;
import java.util.Set;

import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Trainee;

public interface ContractorService {

	Set<TraineeDTO> getContractorsTrainees(Integer id);
	
}
