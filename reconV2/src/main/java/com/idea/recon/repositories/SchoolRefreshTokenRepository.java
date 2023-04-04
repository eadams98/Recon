package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.SchoolRefreshToken;

public interface SchoolRefreshTokenRepository extends JpaRepository<SchoolRefreshToken, Integer> {

	Optional<SchoolRefreshToken> findByToken(String token);
	
}
