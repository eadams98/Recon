package com.idea.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.idea.enums.Grade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "Report")
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reportId;
	private String title;
	private String description;
	private String Rebuttal;
	private Grade grade;
	private LocalDate submissionDate;
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	
	
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
	

}
