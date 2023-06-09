package com.idea.recon.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.idea.recon.enums.Grade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Report")
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reportId;
	private String title;
	private String description;
	private String Rebuttal;
	@Enumerated(EnumType.STRING)
	private Grade grade;
	private LocalDate submissionDate;
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	
	private Integer contractorLinkId;
	private Integer traineeLinkId;
	/*
	 * title VARCHAR(50),
	description VARCHAR(255),
    rebuttal VARCHAR(255),
	grade ENUM("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F+", "F", "F-"),
	submission_date Date,
    week_start_date Date,
    week_end_date Date,
	contractor_link_id INTEGER NOT NULL,
    trainee_link_id INTEGER NOT NULL,
	PRIMARY KEY (report_id),
	FOREIGN KEY (contractor_link_id) REFERENCES Contractor(id),
    FOREIGN KEY (trainee_link_id) REFERENCES Trainee(trainee_id)
	 */
	//private String sentByEmail;
	//private String sentForEmail;
	

}
