package com.idea.recon.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dto.CreateRetortDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.exception.ReportException;
import com.idea.recon.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping(value = "/trainee")
public class TraineeController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ReportService reportService; 

	@GetMapping("/get-contractors")
	ResponseEntity<List<String>> getMyContractorsWhoHaveReports(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestParam(value = "year") Integer year, @RequestParam(value = "month") String month, @RequestHeader (name="Authorization") String token) throws ReportException, Exception{
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getWeeksContainingReports(byEmail, forEmail, token, year, month, true), HttpStatus.OK);
	}

	@GetMapping("/get-report")
	ResponseEntity<ReportDTO> getSchoolVisibleReport(
			@RequestParam(value = "by") String byEmail,
			@RequestParam(value = "for") String forEmail,
			@RequestParam(value = "weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate,
			@RequestParam(value = "weekEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEndDate,
			@RequestHeader(name = "Authorization") String token) throws ReportException, Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getSchoolVisibleReport(byEmail, forEmail, token, weekStartDate, weekEndDate), HttpStatus.OK);
	}

	@PostMapping("/create-retort")
	ResponseEntity<ReportDTO> createTraineeRetort(
			@RequestBody CreateRetortDTO dto,
			@RequestHeader(name = "Authorization") String token) throws ReportException, Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.createTraineeRetort(dto, token), HttpStatus.CREATED);
	}
	
}
