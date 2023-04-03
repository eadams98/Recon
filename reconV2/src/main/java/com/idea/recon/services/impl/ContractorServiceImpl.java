package com.idea.recon.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorService;

@Service
public class ContractorServiceImpl implements ContractorService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ContractorRepository contractoryRepository;
	
	@Autowired
	@Qualifier("jwtContractorDetailsService")
	private UserDetailsService jwtContractorDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Set<TraineeDTO> getContractorsTrainees(Integer id, String token) throws ContractorException {
		
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.findById(id).get();
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		
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
