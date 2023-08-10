package com.idea.recon.services.impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.TraineeException;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.repositories.TraineeRepository;
import com.idea.recon.services.ContractorService;
import com.idea.recon.services.TraineeService;
import com.idea.recon.services.VerificationService;

@Service
public class VerificationServiceImpl implements VerificationService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	TraineeRepository traineeRepository;
	@Autowired 
	TraineeService traineeService;
	@Autowired
	@Qualifier("jwtTraineeDetailsService")
	private UserDetailsService jwtTraineeDetailsService;
	
	@Autowired
	ContractorRepository contractoryRepository;
	@Autowired
	ContractorService contractorService;
	@Autowired
	@Qualifier("jwtContractorDetailsService")
	private UserDetailsService jwtContractorDetailsService;
	
	public RelationshipVerificationDTO route(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException {
		String role = jwtTokenUtil.getRoleFromToken(token);
		if (role.equalsIgnoreCase("trainee"))
			return confirmTraineeToContractorConnection(contractorEmail, traineeEmail, token);
		else if (role.equalsIgnoreCase("contractor"))
			return confirmContractorToTraineeConnection(contractorEmail, traineeEmail, token);
		return null; //throw error
	}
	
	private RelationshipVerificationDTO confirmContractorToTraineeConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.getByEmail(contractorEmail).get();
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		logger.info(user.getUsername() + " vs ");
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		
		Trainee trainee = traineeService.getTraineeByEmail(traineeEmail);
		if (!foundContractor.getTrainees().contains(trainee))
			throw new ContractorException("Contractor.TRAINEE_NOT_LINKED");
		
		RelationshipVerificationDTO relationship = RelationshipVerificationDTO.builder()
				.byId(foundContractor.getId())
				.byName(foundContractor.getFirstName() + " " + foundContractor.getLastName())
				.forId(trainee.getTraineeId())
				.forName(trainee.getFirstName() + " " + trainee.getLastName())
				.build();
		
		return relationship;
	}
	
	private RelationshipVerificationDTO confirmTraineeToContractorConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtTraineeDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Trainee foundTrainee = traineeRepository.getByEmail(traineeEmail).get();
		boolean isSameTrainee = traineeRepository.getByEmail(user.getUsername()).get() == foundTrainee;
		logger.info("requestor id and trainee id match': " + isSameTrainee);
		//logger.info(user.getUsername() + " vs ");
		
		if (!isAdmin && !isSameTrainee)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		
		Contractor contractor = contractorService.getContractorByEmail(contractorEmail);
		if (!contractor.getTrainees().contains(foundTrainee))
			throw new ContractorException("Contractor.TRAINEE_NOT_LINKED"); // change mapping. this is same as contractor
		
		RelationshipVerificationDTO relationship = RelationshipVerificationDTO.builder()
				.byId(contractor.getId())
				.byName(contractor.getFirstName() + " " + contractor.getLastName())
				.forId(foundTrainee.getTraineeId())
				.forName(foundTrainee.getFirstName() + " " + foundTrainee.getLastName())
				.build();
		
		return relationship;
	}

}
