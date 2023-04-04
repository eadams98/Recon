package com.idea.recon.services;

import java.util.Optional;

import com.idea.recon.entities.SchoolRefreshToken;

public interface SchoolRefreshTokenService {
	
	public Optional<SchoolRefreshToken> findByToken(String token);
	public SchoolRefreshToken createRefreshToken(String emailId);
	public SchoolRefreshToken verifyExpiration(SchoolRefreshToken token);
	public int deleteByUserId(Long userId);
	
}
