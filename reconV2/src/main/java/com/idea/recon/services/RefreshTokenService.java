package com.idea.recon.services;

import java.util.Optional;

import com.idea.recon.entities.RefreshToken;

public interface RefreshTokenService {

	public Optional<RefreshToken> findByToken(String token);
	public RefreshToken createRefreshToken(String emailId);
	public RefreshToken verifyExpiration(RefreshToken token);
	public int deleteByUserId(Long userId);
	
}
