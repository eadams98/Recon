package com.idea.recon.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.config.JwtContractorDetailsService;
import com.idea.recon.config.JwtSchoolDetailsService;
import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.config.JwtTraineeDetailsService;
import com.idea.recon.dtos.JwtRequest;
import com.idea.recon.dtos.JwtResponse;
import com.idea.recon.entities.TraineeRefreshToken;
import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.ContractorRefreshToken;
import com.idea.recon.entities.School;
import com.idea.recon.entities.SchoolRefreshToken;
import com.idea.recon.entities.TraineeLogin;
import com.idea.recon.services.ContractorRefreshTokenService;
import com.idea.recon.services.SchoolRefreshTokenService;
import com.idea.recon.services.TraineeRefreshTokenService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class JwtAuthenticationController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Qualifier("traineeAuthenticationManager")
	@Autowired
	private AuthenticationManager traineeAuthenticationManager;
	@Qualifier("contractorAuthenticationManager")
	@Autowired
	private AuthenticationManager contractorAuthenticationManager;
	@Qualifier("schoolAuthenticationManager")
	@Autowired
	private AuthenticationManager schoolAuthenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtTraineeDetailsService traineeDetailsService;
	@Autowired
	private JwtContractorDetailsService contractorDetailsService;
	@Autowired
	private JwtSchoolDetailsService schoolDetailsService;
	
	@Autowired
	TraineeRefreshTokenService traineeRefreshTokenService;
	@Autowired
	ContractorRefreshTokenService contractorRefreshTokenService;
	@Autowired
	SchoolRefreshTokenService schoolRefreshTokenService;

	@RequestMapping(value = "/authenticate/{userType}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@PathVariable String userType, @RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(userType, authenticationRequest.getUsername(), authenticationRequest.getPassword());
		UserDetails userDetails;
		String jwtToken;
		
		switch(userType.toLowerCase()) {
			case "trainee":
				userDetails = traineeDetailsService.loadUserByUsername(authenticationRequest.getUsername()); //Spring core User
				final TraineeLogin traineeDetails = traineeDetailsService.loadTraineeByEmail(authenticationRequest.getUsername());
				
				final TraineeRefreshToken traineeRefreshToken = traineeRefreshTokenService.createRefreshToken(userDetails.getUsername());
				jwtToken = jwtTokenUtil.generateToken(userDetails);
		
				//ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
				//        userDetails.getUsername(), userDetails.getEmail(), roles));
				return ResponseEntity.ok(
						new JwtResponse(
								jwtToken, 
								traineeRefreshToken.getToken(), 
								traineeDetails.getTrainee().getTraineeId(),
								traineeDetails.getTrainee().getFirstName() + " " + traineeDetails.getTrainee().getLastName(),
								traineeDetails.getTrainee().getEmail(), 
								userDetails.getAuthorities()
						)
				);
			case "contractor":
				userDetails = contractorDetailsService.loadUserByUsername(authenticationRequest.getUsername());
				final Contractor contractorDetails = contractorDetailsService.loadTraineeByEmail(authenticationRequest.getUsername());
				
				final ContractorRefreshToken contractorRefreshToken = contractorRefreshTokenService.createRefreshToken(userDetails.getUsername());
				jwtToken = jwtTokenUtil.generateToken(userDetails);
				
				logger.info(userDetails.getAuthorities().toString());
				
				return ResponseEntity.ok(
						new JwtResponse(
								jwtToken, 
								contractorRefreshToken.getToken(), 
								contractorDetails.getId(),
								contractorDetails.getFirstName() + " " + contractorDetails.getLastName(),
								contractorDetails.getEmail(), 
								userDetails.getAuthorities()
						)
				);
				
			case "school":
				userDetails = schoolDetailsService.loadUserByUsername(authenticationRequest.getUsername());
				final School SchoolDetails = schoolDetailsService.loadSchoolByEmail(authenticationRequest.getUsername());
				
				final SchoolRefreshToken schoolRefreshToken = schoolRefreshTokenService.createRefreshToken(userDetails.getUsername());
				jwtToken = jwtTokenUtil.generateToken(userDetails);
				
				logger.info(userDetails.getAuthorities().toString());
				
				return ResponseEntity.ok(
						new JwtResponse(
								jwtToken, 
								schoolRefreshToken.getToken(), 
								SchoolDetails.getSchoolId(),
								SchoolDetails.getSchoolName(),
								SchoolDetails.getEmail(), 
								userDetails.getAuthorities()
						)
				);
				
			default:
				throw new Exception("DEFAULT CASE HIT");
		}
	}

	/*@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}*/

	private void authenticate(String userType, String username, String password) throws Exception {
		try {
			switch(userType.toLowerCase()) {
				case "trainee":
					traineeAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
					break;
				case "contractor":
					try {
					contractorAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
					} catch (BadCredentialsException ex) {
						// Happens if try to login as trainee in body details, but have contractor as userType
						throw new Exception("Invalid credentials. You may have selected the wrong userType. This is for userType contractor");
					} catch (Exception ex) {
						logger.warn(ex.getMessage());
						logger.warn(ex.getClass().getName());
						throw ex;
					}
					break;
				case "school":
					try {
						schoolAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
						} catch (BadCredentialsException ex) {
							// Happens if try to login as trainee in body details, but have contractor as userType
							throw new Exception("Invalid credentials. You may have selected the wrong userType. This is for userType contractor");
						} catch (Exception ex) {
							logger.warn(ex.getMessage());
							logger.warn(ex.getClass().getName());
							throw ex;
						}
					break;
				default:
					throw new Exception("INCORRECT USERTYPE GIVEN. You gave " + userType +  " but the accepted types are (trainee, contractor, school, council)");
				
			}
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}