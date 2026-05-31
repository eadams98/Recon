package com.idea.recon.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRetortDTO {

	private String content;

	/** Contractor email (rating author) */
	private String sentByEmail;

	/** Trainee / junior contractor email */
	private String sentForEmail;

	private LocalDate weekStartDate;

	private LocalDate weekEndDate;
}
