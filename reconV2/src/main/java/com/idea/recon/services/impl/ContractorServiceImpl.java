package com.idea.recon.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.dtos.ContractorDTO;
import com.idea.recon.dtos.RelationshipVerificationDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.TraineeException;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorService;
import com.idea.recon.services.TraineeService;

@Service
@Transactional
public class ContractorServiceImpl implements ContractorService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	TraineeService traineeService;
	
	@Autowired
	ContractorRepository contractoryRepository;
	
	@Autowired
	@Qualifier("jwtContractorDetailsService")
	private UserDetailsService jwtContractorDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Set<TraineeDTO> getContractorsTrainees(Integer id, String token) throws ContractorException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.findById(id).get();
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
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

	@Override
	public ContractorDTO getMyDetails(Integer id, String token) throws ContractorException {
		
		/*
		 * Not a great idea to stream over trainee list and send since it could be large. 
		 * Solutions: 
		 * 1) Make a seperate API for getting trainees with Pagination and sorting
		 * 2) 
		 */
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.findById(id).get();
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		ContractorDTO contractorDetails = ContractorDTO.builder()
				.email(foundContractor.getEmail())
				.firstName(foundContractor.getFirstName())
				.lastName(foundContractor.getLastName())
				.trainees(
						foundContractor.getTrainees().stream()
						.map(trainee -> new TraineeDTO(trainee.getTraineeId(), trainee.getEmail(), trainee.getFirstName(), trainee.getLastName(), null))
						.collect(Collectors.toList())
				)
				.build();
		
		
		return contractorDetails;
	}

	@Override
	@Transactional
	public String updateMyDetails(ContractorDTO updateInfo, String token) throws ContractorException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.findById(updateInfo.getId()).get(); // I think this is bad practice. Should I just get using emailId from DTO
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		// CAN USE DTO ANNOTATIONS VALIDATION AND ADD REQUIRED ANNOTATIONS TO CONTROLLER
		if(updateInfo.getFirstName() != null && !updateInfo.getFirstName().isBlank() && !updateInfo.getFirstName().equals(foundContractor.getFirstName()))
			foundContractor.setFirstName(updateInfo.getFirstName());
		if(updateInfo.getLastName() != null && !updateInfo.getLastName().isBlank() && !updateInfo.getLastName().equals(foundContractor.getLastName()))
			foundContractor.setLastName(updateInfo.getLastName());
		if(updateInfo.getEmail() != null && !updateInfo.getEmail().isBlank() && !updateInfo.getEmail().equals(foundContractor.getEmail()))
			foundContractor.setEmail(updateInfo.getEmail());
		
		
		String fullName = foundContractor.getFirstName().strip();
		if (!foundContractor.getLastName().isBlank())
			fullName += " " + foundContractor.getLastName().strip();
		
		return "Contractor: " + fullName + " with Email: " + foundContractor.getEmail() + " has been updated successfully";
	}

	@Override
	public RelationshipVerificationDTO confirmContractorTraineeConnection(String contractorEmail, String traineeEmail, String token) throws ContractorException, TraineeException {
		
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

	@Override
	public Boolean confirmAccessRequest(Integer id, String token) throws ContractorException, Exception {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtContractorDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Contractor foundContractor = contractoryRepository.findById(id).orElseThrow(() -> new ContractorException("Contractor.NOT_FOUND"));
		boolean isSameContractor = contractoryRepository.getByEmail(user.getUsername()).get() == foundContractor;
		logger.info("requestor id and contractor id match': " + isSameContractor);
		
		if (!isAdmin && !isSameContractor)
			throw new ContractorException("Contractor.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END*/

		return true;
	}

}
