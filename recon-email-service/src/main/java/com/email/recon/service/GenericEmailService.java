package com.email.recon.service;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.email.recon.DTO.ContractorAndTraineeCorrespondenceDTO;
import com.email.recon.DTO.ReportDTO;

public interface GenericEmailService {
	String sendAssigendTraineeToContractor(ContractorAndTraineeCorrespondenceDTO emailInfo);
	String sendWeeklyReportCreated(ContractorAndTraineeCorrespondenceDTO emailInfo);
	String updateWeeklyReport(ContractorAndTraineeCorrespondenceDTO emailInfo);
	String verifyEmailAddress(String email);
}
