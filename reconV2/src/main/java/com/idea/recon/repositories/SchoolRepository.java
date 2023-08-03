package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idea.recon.entities.School;

public interface SchoolRepository extends JpaRepository<School, Integer> {
	Optional<School> getByEmail(String email_id);
	
}