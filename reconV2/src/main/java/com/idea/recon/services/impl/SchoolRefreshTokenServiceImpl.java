package com.idea.recon.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idea.recon.entities.SchoolRefreshToken;
import com.idea.recon.repositories.SchoolRefreshTokenRepository;
import com.idea.recon.repositories.SchoolRepository;
import com.idea.recon.services.SchoolRefreshTokenService;

@Service
public class SchoolRefreshTokenServiceImpl implements SchoolRefreshTokenService {

	@Value("${jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	private SchoolRefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	SchoolRepository schoolRepository;
	
	@Override
	public Optional<SchoolRefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public SchoolRefreshToken createRefreshToken(String emailId) {
		SchoolRefreshToken refreshToken = new SchoolRefreshToken();
		refreshToken.setSchool(schoolRepository.getByEmail(emailId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());
	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	}

	@Override
	public SchoolRefreshToken verifyExpiration(SchoolRefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
		      refreshTokenRepository.delete(token);
		      //throw new RefreshTokenServiceImpl(token.getToken(), "Refresh token was expired. Please make a new signin request");
		    }

		    return token;
	}

	@Override
	public int deleteByUserId(Long userId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
