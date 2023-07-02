package com.idea.recon.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity(name = "School_to_Contractor")
@Data
public class SchoolToContractor {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private LocalDate dateAssigned;
	
	@ManyToOne
	@JoinColumn(name = "school_id")
	private School school;
	
	@ManyToOne
	@JoinColumn(name = "contractor_id")
	private Contractor contractor; 
	
}
