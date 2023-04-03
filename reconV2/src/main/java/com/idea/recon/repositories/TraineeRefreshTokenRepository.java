package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.TraineeRefreshToken;

public interface TraineeRefreshTokenRepository extends JpaRepository<TraineeRefreshToken, Integer> {
	
	Optional<TraineeRefreshToken> findByToken(String token);
	
	//int deleteByUser(User user);
	
}
