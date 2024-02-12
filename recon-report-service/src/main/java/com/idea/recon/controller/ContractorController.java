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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.dto.RelationshipVerificationDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.exception.ReportException;
import com.idea.recon.service.ReportService;


@CrossOrigin
@RestController
@RequestMapping(value = "/contractor")
public class ContractorController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ReportService reportService;
	
	@GetMapping("test")
	ResponseEntity<String> createReport() throws Exception {
		logger.info("HIT TEST");
		return new ResponseEntity<>("Hit Report MS", HttpStatus.CREATED);
	}
	
	@PostMapping("/create-report")
	ResponseEntity<String> createReport(@RequestBody ReportDTO reportDTO, @RequestHeader (name="Authorization") String token) throws Exception {
		logger.info("HIT CREATE");
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.createReport(reportDTO, token), HttpStatus.CREATED);
	}
	
	@PutMapping("/update-report")
	ResponseEntity<ReportDTO> updateReport(@RequestBody ReportDTO reportDTO, @RequestParam("revision") Boolean isRevision, @RequestHeader (name="Authorization") String token) throws ReportException, Exception {
		token = token.split(" ")[1];
		logger.info("isRevision: " + isRevision);
		return new ResponseEntity<>(reportService.updateReport(reportDTO, token), HttpStatus.OK);
	}
	
	@GetMapping("/get-report")
	ResponseEntity<ReportDTO> getReport(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestParam(value="weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate, @RequestParam(value="weekEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEndDate, @RequestHeader (name="Authorization") String token) throws ReportException, Exception {
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getReport(byEmail, forEmail, token, weekStartDate, weekEndDate) , HttpStatus.CREATED);
	}
	
	@GetMapping("/report/years")
	ResponseEntity<List<String>> getYearsThatContainReports(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestHeader (name="Authorization") String token) throws ReportException, Exception{
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getYearsContainingReports(byEmail, forEmail, token), HttpStatus.OK);
	}
	
	@GetMapping("/report/months")
	ResponseEntity<List<String>> getMonthsThatContainReports(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestParam(value = "year") Integer year, @RequestHeader (name="Authorization") String token) throws ReportException, Exception{
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getMonthsContainingReports(byEmail, forEmail, token, year), HttpStatus.OK);
	}
	
	@GetMapping("/report/weeks")
	ResponseEntity<List<String>> getWeekRangeThatContainReports(@RequestParam(value = "by") String byEmail, @RequestParam(value = "for") String forEmail, @RequestParam(value = "year") Integer year, @RequestParam(value = "month") String month, @RequestHeader (name="Authorization") String token) throws ReportException, Exception{
		token = token.split(" ")[1];
		return new ResponseEntity<>(reportService.getWeeksContainingReports(byEmail, forEmail, token, year, month), HttpStatus.OK);
	}
}
