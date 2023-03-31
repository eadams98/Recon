package com.idea.recon.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idea.recon.entities.RefreshToken;
import com.idea.recon.repositories.RefreshTokenRepository;
import com.idea.recon.repositories.TraineeLoginRepository;
import com.idea.recon.repositories.UserRepositories;
import com.idea.recon.services.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	@Value("${jwtRefreshExpirationMs}")
	 private Long refreshTokenDurationMs;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserRepositories userRepository; // traineeRepository

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(String emailId) {
		RefreshToken refreshToken = new RefreshToken();

	    refreshToken.setTrainee(userRepository.getByEmailId(emailId).get()); // set User
	    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());

	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
		      refreshTokenRepository.delete(token);
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
