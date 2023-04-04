package com.idea.recon.services.impl;

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
import com.idea.recon.dtos.SchoolDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.School;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.SchoolException;
import com.idea.recon.repositories.SchoolRepository;
import com.idea.recon.services.SchoolService;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("jwtSchoolDetailsService")
	private UserDetailsService jwtSchoolDetailsService;
	
	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Set<TraineeDTO> getStudents(Integer id, String token) throws SchoolException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtSchoolDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		School foundSchool = schoolRepository.findById(id).get();
		boolean isSameSchool = schoolRepository.getByEmail(user.getUsername()).get() == foundSchool;
		logger.info("requestor id and contractor id match': " + isSameSchool);
		
		if (!isAdmin && !isSameSchool)
			throw new SchoolException("School.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		Set<TraineeDTO> myStudents = foundSchool.getStudents().stream()
				.map(schoolToTrainee -> {
					return TraineeDTO.builder()
						.traineeId(schoolToTrainee.getTrainee().getTraineeId())
						.firstName(schoolToTrainee.getTrainee().getFirstName())
						.lastName(schoolToTrainee.getTrainee().getLastName())
						.email(schoolToTrainee.getTrainee().getEmail())
						.build();
				})
				.collect(Collectors.toSet());
				
		return myStudents;
	}

	@Override
	public SchoolDTO getMyDetails(Integer id, String token) throws SchoolException {
		
		// Could be abstracted out to AOP START
		UserDetails user = jwtSchoolDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		School foundSchool = schoolRepository.findById(id).get();
		boolean isSameSchool = schoolRepository.getByEmail(user.getUsername()).get() == foundSchool;
		logger.info("requestor id and contractor id match': " + isSameSchool);
		
		if (!isAdmin && !isSameSchool)
			throw new SchoolException("School.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
				
		return SchoolDTO.builder()
				.schoolId(foundSchool.getSchoolId())
				.schoolName(foundSchool.getSchoolName())
				.email(foundSchool.getEmail())
				.build();
	}

	@Override
	public String updateMyDetails(SchoolDTO updateInfo, String token) throws SchoolException {
		// Could be abstracted out to AOP START
		UserDetails user = jwtSchoolDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
		logger.info("user authorities: " + user.getAuthorities());
		boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("admin"));
		logger.info("user authorities contains 'admin': " + isAdmin);
		
		School foundSchool = schoolRepository.findById(updateInfo.getSchoolId()).get();
		boolean isSameSchool = schoolRepository.getByEmail(user.getUsername()).get() == foundSchool;
		logger.info("requestor id and contractor id match': " + isSameSchool);
		
		if (!isAdmin && !isSameSchool)
			throw new SchoolException("School.JWT_USER_CONTRACTOR_MISMATCH");
		// Could be abstracted out to AOP END
		
		// CAN USE DTO ANNOTATIONS VALIDATION AND ADD REQUIRED ANNOTATIONS TO CONTROLLER
		if(updateInfo.getSchoolName() != null && !updateInfo.getSchoolName().isBlank() && !updateInfo.getSchoolName().equals(foundSchool.getSchoolName()))
				foundSchool.setSchoolName(updateInfo.getSchoolName());
		
		return "School: " + foundSchool.getSchoolName() + " with Email: " + foundSchool.getEmail() + " has been updated successfully";
	}

	@Override
	public School loadSchoolByEmail(String eamil) throws SchoolException {
		School school = schoolRepository.getByEmail(eamil).orElseThrow(() -> new SchoolException("School.NOT_FOUND"));
		return school;
	}

}
