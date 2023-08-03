package com.idea.recon.service.impl;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idea.recon.dto.ContractorAndTraineeCorrespondenceDTO;
import com.idea.recon.dto.RelationshipVerificationDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.entity.Report;
import com.idea.recon.enums.Grade;
import com.idea.recon.exception.MicroserviceException;
import com.idea.recon.exception.ReportException;
import com.idea.recon.repository.ReportRepository;
import com.idea.recon.service.ReportService;
import com.idea.recon.utility.ErrorInfo;
import com.idea.recon.utility.MicroserviceUtil;


@Service(value = "reportService")
public class ReportServiceImpl implements ReportService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	MicroserviceUtil microserviceUtil;
	
	@Autowired
	ReportRepository reportRepository;
	
	@Override
	public ReportDTO getReport(String byEmail, String forEmail, String token, LocalDate startOfWeek, LocalDate endOfWeek) throws ReportException, Exception {
		ResponseEntity<RelationshipVerificationDTO> response = verifyIdentity(token, byEmail, forEmail);
		RelationshipVerificationDTO relationship = response.getBody();
		
		String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		
		if (!Pattern.matches(regex, startOfWeek.toString()) || !Pattern.matches(regex, endOfWeek.toString()))
			throw new Exception("incorrect start or end date");
		
		Optional<Report> optionalReport = reportRepository.getSpecificReport(relationship.getById(), relationship.getForId(), startOfWeek, endOfWeek);
		logger.info(optionalReport.toString());
		
		if (optionalReport.isEmpty())
			throw new Exception("No Report found for given date range: " + startOfWeek + " - " + endOfWeek);
		
		Report report = optionalReport.get();
		logger.info(report.toString()); 
		
		String rebuttal = report.getRebuttal() == null ? "" : report.getRebuttal();
		
		return ReportDTO.builder()
				.reportId(report.getReportId())
				.description(report.getDescription())
				.grade(Grade.toString(report.getGrade()))
				.rebuttal(rebuttal)
				.submissionDate(report.getSubmissionDate())
				.weekStartDate(report.getWeekStartDate())
				.weekEndDate(report.getWeekEndDate())
				//.sentForEmail(forEmail)
				.title(report.getTitle())
				.build();
	}
	
	@Override
	@Transactional
	public ReportDTO updateReport(ReportDTO reportDTO, String token) throws ReportException, Exception {
		
		// Verify that this report exists
		Report report = reportRepository.findById(reportDTO.getReportId()).orElseThrow(() -> new ReportException("Report.NOT_FOUND"));
		
		// Verify If contractor or Admin (If contractor does this report belong to them, if not throw error)
		
		
		if (!reportDTO.getDescription().equals(report.getDescription()))
			report.setDescription(reportDTO.getDescription());
		if (!reportDTO.getTitle().equals(report.getTitle()))
			report.setTitle(report.getTitle());
		if (!reportDTO.getGrade().equals(Grade.toString(report.getGrade())))
			report.setGrade(Grade.fromString(reportDTO.getGrade()));
		// either add revision time stamp on Report Entity here or update submissionDate. Leaning more toward new column/field

		String rebuttal = report.getRebuttal() == null ? "" : report.getRebuttal();
		return ReportDTO.builder()
				.reportId(report.getReportId())
				.description(report.getDescription())
				.grade(Grade.toString(report.getGrade()))
				.rebuttal(rebuttal)
				.submissionDate(report.getSubmissionDate())
				.weekStartDate(report.getWeekStartDate())
				.weekEndDate(report.getWeekEndDate())
				//.sentForEmail(forEmail)
				.title(report.getTitle())
				.build();
	}

	@Override
	public String createReport(ReportDTO reportDTO, String token) throws ReportException, Exception {
		
		ResponseEntity<RelationshipVerificationDTO> response = verifyIdentity(token, reportDTO.getSentByEmail(), reportDTO.getSentForEmail());
		
		LocalDate startOfWeek = reportDTO.getWeekStartDate() != null ? getStartOfWeek(reportDTO.getWeekStartDate()) : getStartOfWeek(LocalDate.now());
		LocalDate endOfWeek = reportDTO.getWeekEndDate() != null ? getEndOfWeek(reportDTO.getWeekEndDate()) : getEndOfWeek(LocalDate.now());
		LocalDate currentDate = LocalDate.now();
		
		if (reportRepository.contractorReportExist(response.getBody().getById(), response.getBody().getForId(), startOfWeek, endOfWeek))
			throw new ReportException("Report.Contractor.DUPLICATE_REPORT");
		
		Report report = Report.builder()
				.title(reportDTO.getTitle())
				.description(reportDTO.getDescription())
				.grade(Grade.fromString(reportDTO.getGrade()))
				.submissionDate(currentDate)
				.weekStartDate(startOfWeek)
				.weekEndDate(endOfWeek)
				.contractorLinkId(response.getBody().getById())
				.traineeLinkId(response.getBody().getForId())
				.build();
		
		report = reportRepository.save(report);
		//ResponseEntity<String> emailResponse = sendCreatedReportEmail(token, response.getBody().getByName(), reportDTO.getSentByEmail(), response.getBody().getForName(), reportDTO.getSentForEmail(), report.toReportDTO(response.getBody().getByEmail(), response.getBody().getForEmail()));

		logger.info(report.toString());
		
		return "Report Created.";
	}
	
	@Override
	public List<String> getYearsContainingReports(String byEmail, String forEmail, String token) throws ReportException, Exception {
		ResponseEntity<RelationshipVerificationDTO> response = verifyIdentity(token, byEmail, forEmail);
		RelationshipVerificationDTO relationship = response.getBody();
		return reportRepository.getYearsWithReportsOfContractorWithTrainee(relationship.getById(), relationship.getForId());
	}
	
	@Override
	public List<String> getMonthsContainingReports(String byEmail, String forEmail, String token, Integer year)
			throws ReportException, Exception {
		ResponseEntity<RelationshipVerificationDTO> response = verifyIdentity(token, byEmail, forEmail);
		RelationshipVerificationDTO relationship = response.getBody();
		return reportRepository.getMonthsWithReportsOfContractorWithTrainee(relationship.getById(), relationship.getForId(), year);
	}

	@Override
	public List<String> getWeeksContainingReports(String byEmail, String forEmail, String token, Integer year,
			String month) throws ReportException, Exception {
		ResponseEntity<RelationshipVerificationDTO> response = verifyIdentity(token, byEmail, forEmail);
		RelationshipVerificationDTO relationship = response.getBody();
		
		Map<String, Integer> monthStringToMonthInteger = new HashMap<>();
		monthStringToMonthInteger.put("january", 1);
		monthStringToMonthInteger.put("jan", 1);
		monthStringToMonthInteger.put("february", 2);
		monthStringToMonthInteger.put("feb", 2);
		monthStringToMonthInteger.put("march", 3);
		monthStringToMonthInteger.put("mar", 3);
		monthStringToMonthInteger.put("april", 4);
		monthStringToMonthInteger.put("apr", 4);
		monthStringToMonthInteger.put("may", 5);
		monthStringToMonthInteger.put("june", 6);
		monthStringToMonthInteger.put("jun", 6);
		monthStringToMonthInteger.put("july", 7);
		monthStringToMonthInteger.put("jul", 7);
		monthStringToMonthInteger.put("august", 8);
		monthStringToMonthInteger.put("aug", 8);
		monthStringToMonthInteger.put("september", 9);
		monthStringToMonthInteger.put("sep", 9);
		monthStringToMonthInteger.put("october", 10);
		monthStringToMonthInteger.put("oct", 10);
		monthStringToMonthInteger.put("november", 11);
		monthStringToMonthInteger.put("nov", 11);
		monthStringToMonthInteger.put("december", 12);
		monthStringToMonthInteger.put("dec", 12);
		
		int monthInt;
		month = month.toLowerCase();
		if (monthStringToMonthInteger.containsKey(month))
			monthInt = monthStringToMonthInteger.get(month);
		else
			monthInt = Integer.valueOf(month);
		
		return reportRepository.getWeeksWithReportsOfContractorWithTrainee(relationship.getById(), relationship.getForId(), year, monthInt);
	}
	
	// PRIVATE METHDOS 
	private static LocalDate getStartOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int daysToSubtract = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(daysToSubtract);
    }

    private static LocalDate getEndOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int daysToAdd = DayOfWeek.SUNDAY.getValue() - dayOfWeek.getValue();
        return date.plusDays(daysToAdd);
    }
    
    private ResponseEntity<RelationshipVerificationDTO> verifyIdentity(String token, String sentByEmail, String sentForEmail) throws MicroserviceException {
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		

		HttpEntity<String> entity = new HttpEntity<>(headers);
		String url = "http://user-service/contractor/verify?by=" + sentByEmail + "&for=" + sentForEmail;
		ResponseEntity<RelationshipVerificationDTO> response = null; 
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, entity, RelationshipVerificationDTO.class);
			logger.info("AFTER RESPONE: " + response.getBody().toString());
		} catch (Exception ex) {
			microserviceUtil.handleHttpClientExceptionAndHttpServerException(ex);
			logger.info("RESPONSE FROM USER MS CALL: " + ex.getClass().toString()); 
		}
		
		return response;
    }
    
    private ResponseEntity<String> sendCreatedReportEmail(String token, String sentByName, String sentByEmail, String sentForName, String sentForEmail, ReportDTO report) throws MicroserviceException {
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		
		String url = "http://email-service/email/report-created";
		ResponseEntity<String> response = null; 
		
		ContractorAndTraineeCorrespondenceDTO data = ContractorAndTraineeCorrespondenceDTO.builder()
				.weekRange(report.getWeekStartDate() + " - " + report.getWeekEndDate())
				.reportDTO(report)
				.contractorName(sentByName)
				.contractorEmail("eric051598@gmail.com")
				.contractorMessageSubject("")
				.contractorMessageBody("")
				.traineeName(sentForName)
				.traineeEmail("eric051598@gmail.com")
				.traineeMessageSubject("")
				.traineeMessageBody("")
				.build();
		HttpEntity<ContractorAndTraineeCorrespondenceDTO> entity = new HttpEntity<>(data, headers);

		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			//restTemplate.postFor
			logger.info("AFTER RESPONE: " + response.getBody().toString());
		} catch (Exception ex) {
			microserviceUtil.handleHttpClientExceptionAndHttpServerException(ex);
			logger.info("RESPONSE FROM USER MS CALL: " + ex.getClass().toString()); 
		}
		
		return response;
    }

}
