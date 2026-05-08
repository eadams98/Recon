package com.idea.recon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReportDTO {
	private Integer reportId;
	private String title;
	private String description;
	private String rebuttal;
	/** Text from the optional {@code Retort} entity (distinct from legacy {@code rebuttal}). */
	private String retortContent;
	private String grade;
	private LocalDate submissionDate; // ALWAYS GOTTEN FROM BACKEND
	private LocalDate weekStartDate; // Start and End can be gotten from a single given date.
	private LocalDate weekEndDate; // Might remove these in favor of just a single date
	private Boolean isFinalized;
	private LocalDateTime finalizedAt;
	
	// Not going to use entities because users should be contained
	// in user-service
	private String sentByEmail;
	private String sentForEmail;
}
