package com.idea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idea.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}
