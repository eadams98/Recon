package com.idea.recon.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.idea.recon.enums.Grade;

@Entity
public class Report {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer reportId;
	String title;
	String description;
	Grade grade;
	LocalDateTime submissionDate;
	
}
