package com.idea.recon.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.idea.recon.dto.RelationshipVerificationDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.entity.Report;
import com.idea.recon.exception.ReportException;

public interface ReportService {
	
	public String createReport(ReportDTO reportDTO, String token) throws ReportException, Exception;
	public ReportDTO getReport(String byEmail, String forEmail, String token, LocalDate startOfWeek, LocalDate endOfWeek) throws ReportException, Exception;
	public ReportDTO updateReport(ReportDTO reportDTO, String token)throws ReportException, Exception;
	
	public List<String> getYearsContainingReports(String byEmail, String forEmail, String token) throws ReportException, Exception;
	public List<String> getMonthsContainingReports(String byEmail, String forEmail, String token, Integer year) throws ReportException, Exception;
	public List<String> getWeeksContainingReports(String byEmail, String forEmail, String token, Integer year, String month) throws ReportException, Exception;

}
