package com.idea.recon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entities.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, Integer> {

}
