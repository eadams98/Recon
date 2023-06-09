package com.idea.recon.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idea.recon.entities.TraineeRefreshToken;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.repositories.TraineeRefreshTokenRepository;
import com.idea.recon.repositories.TraineeLoginRepository;
import com.idea.recon.repositories.TraineeRepository;
import com.idea.recon.services.TraineeRefreshTokenService;

@Service
public class TraineeRefreshTokenServiceImpl implements TraineeRefreshTokenService {
	
	@Value("${jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	private TraineeRefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private TraineeRepository traineeRepository; // traineeRepository

	@Override
	public Optional<TraineeRefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public TraineeRefreshToken createRefreshToken(String emailId) {
		TraineeRefreshToken refreshToken = new TraineeRefreshToken();
		refreshToken.setTrainee(traineeRepository.getByEmail(emailId).get()); // set User
	    
	    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());

	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	}

	@Override
	public TraineeRefreshToken verifyExpiration(TraineeRefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
		      //refreshTokenRepository.delete(token);
		      //throw new RefreshTokenServiceImpl(token.getToken(), "Refresh token was expired. Please make a new signin request");
		    }

		    return token;
	}

	
	@Override
	public int deleteByUserId(Long userId) {
		//return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
		return 0;
	}
	

}
