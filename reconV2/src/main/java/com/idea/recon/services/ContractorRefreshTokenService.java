package com.idea.recon.services;

import java.util.Optional;

import com.idea.recon.entities.ContractorRefreshToken;

public interface ContractorRefreshTokenService {
	public Optional<ContractorRefreshToken> findByToken(String token);
	public ContractorRefreshToken createRefreshToken(String emailId);
	public ContractorRefreshToken verifyExpiration(ContractorRefreshToken token);
	public int deleteByUserId(Long userId);
}
