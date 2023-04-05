package com.idea.dto;

import java.time.LocalDate;

import com.idea.enums.Grade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDTO {
	private Integer reportId;
	private String title;
	private String description;
	private String Rebuttal;
	private Grade grade;
	private LocalDate submissionDate;
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	
	// Not going to use entities because users should be contained
	// in user-service
}
