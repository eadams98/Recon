package com.idea.recon.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dto.ReportDTO;
import com.idea.recon.exception.ReportException;
import com.idea.recon.service.ReportService;

/**
 * School users read finalized ratings for students on their roster (user-service verifies school JWT +
 * trainee belongs to school).
 */
@CrossOrigin
@RestController
@RequestMapping("/school")
public class SchoolReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/report/years")
	ResponseEntity<List<String>> getYears(
			@RequestParam(value = "by") String contractorEmail,
			@RequestParam(value = "for") String traineeEmail,
			@RequestHeader(name = "Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(
				reportService.getSchoolPortalYearsContainingReports(contractorEmail, traineeEmail, token), HttpStatus.OK);
	}

	@GetMapping("/report/months")
	ResponseEntity<List<String>> getMonths(
			@RequestParam(value = "by") String contractorEmail,
			@RequestParam(value = "for") String traineeEmail,
			@RequestParam(value = "year") Integer year,
			@RequestHeader(name = "Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(
				reportService.getSchoolPortalMonthsContainingReports(contractorEmail, traineeEmail, token, year),
				HttpStatus.OK);
	}

	@GetMapping("/report/weeks")
	ResponseEntity<List<String>> getWeeks(
			@RequestParam(value = "by") String contractorEmail,
			@RequestParam(value = "for") String traineeEmail,
			@RequestParam(value = "year") Integer year,
			@RequestParam(value = "month") String month,
			@RequestHeader(name = "Authorization") String token) throws Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(
				reportService.getSchoolPortalWeeksContainingReports(contractorEmail, traineeEmail, token, year, month),
				HttpStatus.OK);
	}

	@GetMapping("/get-report")
	ResponseEntity<ReportDTO> getReport(
			@RequestParam(value = "by") String contractorEmail,
			@RequestParam(value = "for") String traineeEmail,
			@RequestParam(value = "weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate,
			@RequestParam(value = "weekEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEndDate,
			@RequestHeader(name = "Authorization") String token) throws ReportException, Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(
				reportService.getSchoolPortalReport(contractorEmail, traineeEmail, token, weekStartDate, weekEndDate),
				HttpStatus.OK);
	}
}
