package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.ContractorRefreshToken;

public interface ContractorRefreshTokenRepository extends JpaRepository<ContractorRefreshToken, Integer> {

	Optional<ContractorRefreshToken> findByToken(String token);
	
}
