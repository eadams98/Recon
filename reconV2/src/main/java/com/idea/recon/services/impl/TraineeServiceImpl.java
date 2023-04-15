package com.idea.recon.services.impl;

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
import com.idea.recon.entities.Trainee;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.TraineeException;
import com.idea.recon.repositories.TraineeRepository;
import com.idea.recon.services.TraineeService;

@Service
public class TraineeServiceImpl implements TraineeService {
	
private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TraineeRepository traineeRepository;
	
	@Autowired
	@Qualifier("jwtTraineeDetailsService")
	private UserDetailsService jwtTraineeDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public ContractorDTO getSupervisor(Integer id, String token) throws TraineeException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtTraineeDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Trainee foundTrainee = traineeRepository.findById(id).get();
		boolean isSameTrainee = traineeRepository.getByEmail(user.getUsername()).get() == foundTrainee;
		logger.info("requestor id and contractor id match': " + isSameTrainee);
		
		if (!isAdmin && !isSameTrainee)
			throw new TraineeException("Trainee.JWT_USER_TRAINEE_MISMATCH");
		// Could be abstracted out to AOP END
		
		ContractorDTO supervisor = null;
		if (foundTrainee.getSupervisor() != null) {
			supervisor = ContractorDTO.builder()
					.email(foundTrainee.getSupervisor().getEmail())
					.firstName(foundTrainee.getSupervisor().getFirstName())
					.lastName(foundTrainee.getSupervisor().getLastName())
					.build();
		}
		
		return supervisor;
	}

	@Override
	public TraineeDTO getMyDetails(Integer id, String token) throws TraineeException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtTraineeDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Trainee foundTrainee = traineeRepository.findById(id).get();
		boolean isSameTrainee = traineeRepository.getByEmail(user.getUsername()).get() == foundTrainee;
		logger.info("requestor id and contractor id match': " + isSameTrainee);
		
		if (!isAdmin && !isSameTrainee)
			throw new TraineeException("Trainee.JWT_USER_TRAINEE_MISMATCH");
		// Could be abstracted out to AOP END
		
		ContractorDTO supervisor = null;
		if (foundTrainee.getSupervisor() != null) {
			supervisor = ContractorDTO.builder()
					.email(foundTrainee.getSupervisor().getEmail())
					.firstName(foundTrainee.getSupervisor().getFirstName())
					.lastName(foundTrainee.getSupervisor().getLastName())
					.build();
		}
		
		
		TraineeDTO traineeDetails = TraineeDTO.builder()
				.email(foundTrainee.getEmail())
				.firstName(foundTrainee.getFirstName())
				.lastName(foundTrainee.getLastName())
				.supervisor(supervisor)
				.build();

		return traineeDetails;
	}

	@Override
	public String updateMyDetails(TraineeDTO updateInfo, String token) throws TraineeException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtTraineeDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		Trainee foundTrainee = traineeRepository.findById(updateInfo.getTraineeId()).get();
		boolean isSameTrainee = traineeRepository.getByEmail(user.getUsername()).get() == foundTrainee;
		logger.info("requestor id and contractor id match': " + isSameTrainee);
		
		if (!isAdmin && !isSameTrainee)
			throw new TraineeException("Trainee.JWT_USER_TRAINEE_MISMATCH");
		// Could be abstracted out to AOP END
		
		if(updateInfo.getFirstName() != null && !updateInfo.getFirstName().isBlank() && !updateInfo.getFirstName().equals(foundTrainee.getFirstName()))
			foundTrainee.setFirstName(updateInfo.getFirstName());
		if(updateInfo.getLastName() != null && !updateInfo.getLastName().isBlank() && !updateInfo.getLastName().equals(foundTrainee.getLastName()))
			foundTrainee.setLastName(updateInfo.getLastName());
		if(updateInfo.getEmail() != null && !updateInfo.getEmail().isBlank() && !updateInfo.getEmail().equals(foundTrainee.getEmail()))
			foundTrainee.setEmail(updateInfo.getEmail());
		
		
		String fullName = foundTrainee.getFirstName().strip();
		if (!foundTrainee.getLastName().isBlank())
			fullName += " " + foundTrainee.getLastName().strip();
		
		return "Contractor: " + fullName + " with Email: " + foundTrainee.getEmail() + " has been updated successfully";

	}

	@Override
	public Trainee getTraineeByEmail(String email) throws TraineeException {
		return traineeRepository.getByEmail(email).orElseThrow(() -> new TraineeException("Trainee.NOT_FOUND"));
	}
	

}
