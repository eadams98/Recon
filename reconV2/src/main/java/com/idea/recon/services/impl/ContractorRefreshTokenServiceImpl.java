package com.idea.recon.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idea.recon.entities.ContractorRefreshToken;
import com.idea.recon.repositories.ContractorRefreshTokenRepository;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorRefreshTokenService;

@Service
public class ContractorRefreshTokenServiceImpl implements ContractorRefreshTokenService {

	@Value("${jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;
	
	@Autowired
	private ContractorRefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	ContractorRepository contractorRepository;
	
	@Override
	public Optional<ContractorRefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public ContractorRefreshToken createRefreshToken(String emailId) {
		ContractorRefreshToken refreshToken = new ContractorRefreshToken();
		refreshToken.setContractor(contractorRepository.getByEmail(emailId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
	    refreshToken.setToken(UUID.randomUUID().toString());
	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	}

	@Override
	public ContractorRefreshToken verifyExpiration(ContractorRefreshToken token) {
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
