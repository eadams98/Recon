package com.idea.recon.controllers;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.config.JwtContractorDetailsService;
import com.idea.recon.config.JwtSchoolDetailsService;
import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.config.JwtTraineeDetailsService;
import com.idea.recon.entities.ContractorRefreshToken;
import com.idea.recon.entities.SchoolRefreshToken;
import com.idea.recon.entities.TraineeRefreshToken;
import com.idea.recon.exceptions.RefreshTokenException;
import com.idea.recon.services.ContractorRefreshTokenService;
import com.idea.recon.services.SchoolRefreshTokenService;
import com.idea.recon.services.TraineeRefreshTokenService;

@RestController
@CrossOrigin
@RequestMapping("/refresh")
public class RefreshTokenController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JwtTraineeDetailsService traineeDetailsService;
	@Autowired
	private JwtContractorDetailsService contractorDetailsService;
	@Autowired
	private JwtSchoolDetailsService schoolDetailsService;
	
	@Autowired
	private ContractorRefreshTokenService contractorRefreshTokenService;
	@Autowired
	private TraineeRefreshTokenService traineeRefreshTokenService;
	@Autowired
	private SchoolRefreshTokenService schoolRefreshTokenService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@GetMapping(value="/{userType}/{token}", produces="application/json")
	public String getNewAccessToken(@PathVariable String userType, @PathVariable String token) throws Exception {
		switch(userType.toLowerCase()) {
			case "contractor":
				Optional<ContractorRefreshToken> optionalContractorRefreshToken = contractorRefreshTokenService.findByToken(token);
				ContractorRefreshToken contractorRefreshToken = optionalContractorRefreshToken.orElseThrow(() -> new RefreshTokenException("Refresh.TOKEN_NOT_FOUND") );
				contractorRefreshToken = contractorRefreshTokenService.verifyExpiration(contractorRefreshToken);
				if (contractorRefreshToken != null) {
					logger.info("Contractor refresh Token Found");
					return jwtTokenUtil.generateToken(contractorDetailsService.loadUserByUsername(contractorRefreshToken.getContractor().getEmail()));
				}
				break;
			case "trainee":
				Optional<TraineeRefreshToken> optionalTraineeRefreshToken = traineeRefreshTokenService.findByToken(token);
				TraineeRefreshToken traineeRefreshToken = optionalTraineeRefreshToken.orElseThrow(() -> new RefreshTokenException("Refresh.TOKEN_NOT_FOUND") );
				traineeRefreshToken = traineeRefreshTokenService.verifyExpiration(traineeRefreshToken);
				if (traineeRefreshToken != null) {
					logger.info("Trainee refresh Token Found");
					return jwtTokenUtil.generateToken(traineeDetailsService.loadUserByUsername(traineeRefreshToken.getTrainee().getEmail()));
				}
				break;
			case "school":
				Optional<SchoolRefreshToken> optionalSchoolRefreshToken = schoolRefreshTokenService.findByToken(token);
				SchoolRefreshToken schoolRefreshToken = optionalSchoolRefreshToken.orElseThrow(() -> new RefreshTokenException("Refresh.TOKEN_NOT_FOUND"));
				schoolRefreshToken = schoolRefreshTokenService.verifyExpiration(schoolRefreshToken);
				if (schoolRefreshToken != null) {
					logger.info("School refresh Token Found");
					return jwtTokenUtil.generateToken(schoolDetailsService.loadUserByUsername(schoolRefreshToken.getSchool().getEmail()));
				}
		}
		return "NULL";
	}

}
