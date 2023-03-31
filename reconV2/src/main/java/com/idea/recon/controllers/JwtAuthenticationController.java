package com.idea.recon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.config.JwtTokenUtil;
import com.idea.recon.config.JwtTraineeDetailsService;
import com.idea.recon.dtos.JwtRequest;
import com.idea.recon.dtos.JwtResponse;
import com.idea.recon.entities.RefreshToken;
import com.idea.recon.entities.TraineeLogin;
import com.idea.recon.services.RefreshTokenService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtTraineeDetailsService userDetailsService;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername()); //Spring core User
		final TraineeLogin traineeDetails = userDetailsService.loadTraineeByEmail(authenticationRequest.getUsername());
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
		final String jwtToken = jwtTokenUtil.generateToken(userDetails);

		//ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
		//        userDetails.getUsername(), userDetails.getEmail(), roles));
		return ResponseEntity.ok(
				new JwtResponse(
						jwtToken, 
						refreshToken.getToken(), 
						traineeDetails.getTrainee().getTraineeId(),
						traineeDetails.getTrainee().getFirstName() + " " + traineeDetails.getTrainee().getLastName(),
						traineeDetails.getTrainee().getEmail(), 
						userDetails.getAuthorities()
				)
		);
	}

	/*@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}*/

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}