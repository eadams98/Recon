package com.idea.recon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.recon.entity.Retort;

public interface RetortRepository extends JpaRepository<Retort, Integer> {

	Optional<Retort> findByReport_ReportId(Integer reportId);
}
