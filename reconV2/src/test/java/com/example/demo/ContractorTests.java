package com.example.demo;

import static org.mockito.ArgumentMatchers.anyInt;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.Role;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorService;
import com.idea.recon.services.impl.ContractorServiceImpl;

/*@SpringBootTest
public class ContractorTests {
	
	@Mock
	ContractorRepository contractorRepo;
	
	@InjectMocks
	ContractorService contractorService = new ContractorServiceImpl();
	
	@Test
	public void authenticateContractorTestValidCredentialsReturnTrainees() throws ContractorException {
		// Simulated Request info
		Integer id = 1;
		
		// Mock Return obj
		Role role = new Role();
		role.setRole_id(0);
		role.setName("contractor");
		
		
		
		Contractor foundContractor = new Contractor();
		foundContractor.setId(id);
		foundContractor.setEmail("test.1@yahoo.com");
		foundContractor.setFirstName("test");
		foundContractor.setLastName("1");
		foundContractor.setPassword(null);
		foundContractor.setRole(role);
		foundContractor.setTrainees(null);
		
		
		// Mocks behavior
		Mockito.when(contractorRepo.findById(anyInt())).thenReturn(Optional.of(foundContractor));
		
		// Test
		Set<TraineeDTO> actual = contractorService.getContractorsTrainees(id, null);
		Assertions.assertEquals(null, actual);
	}

}*/
