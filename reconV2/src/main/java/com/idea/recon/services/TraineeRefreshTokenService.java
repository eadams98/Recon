package com.idea.recon.services;

import java.util.Optional;

import com.idea.recon.entities.TraineeRefreshToken;

public interface TraineeRefreshTokenService {

	public Optional<TraineeRefreshToken> findByToken(String token);
	public TraineeRefreshToken createRefreshToken(String emailId);
	public TraineeRefreshToken verifyExpiration(TraineeRefreshToken token);
	public int deleteByUserId(Long userId);
	
}
