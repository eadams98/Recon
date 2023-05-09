package com.email.recon.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.email.recon.DTO.ContractorAndTraineeCorrespondenceDTO;
import com.email.recon.DTO.ReportDTO;
import com.email.recon.service.GenericEmailService;

@Controller
@RequestMapping(value = "/email")
public class TestEmailer {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	GenericEmailService genericEmailService;
	
	@PostMapping(value = "/contractor-add-trainee")
	ResponseEntity<String> sendEmailToMyself(@RequestBody ContractorAndTraineeCorrespondenceDTO emailInfo) {
		return new ResponseEntity<>(genericEmailService.sendAssigendTraineeToContractor(emailInfo), HttpStatus.OK);
	}
	
	@PostMapping(value = "/register-email/{email}")
	ResponseEntity<String> registerEmail(@PathVariable String email) {
		return new ResponseEntity<>(genericEmailService.verifyEmailAddress(email), HttpStatus.OK);
	}
	
	@PostMapping(value = "/report-created")
	ResponseEntity<String> createdReport(@RequestBody ContractorAndTraineeCorrespondenceDTO emailInfo ) {
		return new ResponseEntity<>(genericEmailService.sendWeeklyReportCreated(emailInfo), HttpStatus.OK);
	}
	
	@PostMapping(value = "/update-report")
	ResponseEntity<String> updateReport(@RequestBody ContractorAndTraineeCorrespondenceDTO emailInfo) {
		return new ResponseEntity<>(genericEmailService.updateWeeklyReport(emailInfo), HttpStatus.OK);
	}

}
