package com.idea.recon.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idea.recon.entities.ContractorRefreshToken;
import com.idea.recon.repositories.ContractorRefreshTokenRepository;
import com.idea.recon.repositories.ContractorRepository;
import com.idea.recon.services.ContractorRefreshTokenService;

@Service
@Transactional
public class ContractorRefreshTokenServiceImpl implements ContractorRefreshTokenService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

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
		if (token == null) {
				logger.warn("EMPTY TOKEN");
				return token;
		}
		
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
