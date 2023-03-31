package com.idea.recon.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorService;

@Service
public class ContractorServiceImpl implements ContractorService {
	
	@Autowired
	ContractorRepository contractoryRepository;

	@Override
	public Set<TraineeDTO> getContractorsTrainees(Integer id) {
		
		Contractor foundContractor = contractoryRepository.findById(id).get();
		
		Set<TraineeDTO> contractorTrainees = new HashSet<>();
		ContractorDTO foundContractorDTO = ContractorDTO.builder()
				.id(foundContractor.getId())
				.firstName(foundContractor.getFirstName())
				.lastName(foundContractor.getLastName())
				.email(foundContractor.getEmail())
				.build();
				
		foundContractor.getTrainees().stream().forEach(trainee -> {
			
					TraineeDTO tDTO = TraineeDTO.builder()
						.traineeId(trainee.getTraineeId())
						.email(trainee.getEmail())
						.firstName(trainee.getFirstName())
						.lastName(trainee.getLastName())
						//.supervisor(foundContractorDTO)
						.build();
					contractorTrainees.add(tDTO);
		});
		
		return contractorTrainees;
	}

}
