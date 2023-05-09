package com.email.recon.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractorAndTraineeCorrespondenceDTO {
	private String weekRange;
	private ReportDTO reportDTO;
	
	//Contractor
	private String contractorName;
	private String contractorEmail; // either make own Address class or add DTO validation
	private String contractorMessageSubject;
	private String contractorMessageBody;
	
	//Trainee
	private String traineeName;
	private String traineeEmail;
	private String traineeMessageSubject;
	private String traineeMessageBody;
}
