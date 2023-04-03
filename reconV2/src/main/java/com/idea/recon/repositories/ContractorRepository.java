package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, Integer> {
	Optional<Contractor> getByEmail(String email_id);
}
