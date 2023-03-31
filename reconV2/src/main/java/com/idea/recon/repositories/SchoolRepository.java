package com.idea.recon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.School;

public interface SchoolRepository extends JpaRepository<School, Integer> {
	
}
