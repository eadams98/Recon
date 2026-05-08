package com.idea.recon.service;

import java.time.LocalDate;
import java.util.List;

import com.idea.recon.dto.CreateRetortDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.exception.ReportException;

public interface ReportService {
	
	public String createReport(ReportDTO reportDTO, String token) throws ReportException, Exception;
	public ReportDTO getReport(String byEmail, String forEmail, String token, LocalDate startOfWeek, LocalDate endOfWeek) throws ReportException, Exception;

	/** Trainee / school read: same contract as {@link #getReport} but only returns a finalized report. */
	public ReportDTO getSchoolVisibleReport(String byEmail, String forEmail, String token, LocalDate startOfWeek, LocalDate endOfWeek) throws ReportException, Exception;
	public ReportDTO updateReport(ReportDTO reportDTO, String token)throws ReportException, Exception;
	
	public List<String> getYearsContainingReports(String byEmail, String forEmail, String token) throws ReportException, Exception;
	public List<String> getMonthsContainingReports(String byEmail, String forEmail, String token, Integer year) throws ReportException, Exception;
	public List<String> getWeeksContainingReports(String byEmail, String forEmail, String token, Integer year, String month) throws ReportException, Exception;

	/**
	 * @param finalizedOnly when true, only weeks with a finalized report are returned (school / trainee listings).
	 *        When false, all saved reports for that relationship are included (contractor tooling).
	 */
	public List<String> getWeeksContainingReports(String byEmail, String forEmail, String token, Integer year, String month, boolean finalizedOnly) throws ReportException, Exception;

	/** Contractor finalizes a rating for the given week (draft becomes visible to school rules). */
	public ReportDTO finalizeReport(String byEmail, String forEmail, String token, LocalDate weekStart, LocalDate weekEnd) throws ReportException, Exception;

	/**
	 * Trainee (junior) authors the single allowed retort for a finalized report in that week.
	 * Must be called with a trainee JWT; enforced via user-service trainee-only verify endpoint.
	 */
	public ReportDTO createTraineeRetort(CreateRetortDTO dto, String token) throws ReportException, Exception;

	// --- School portal (school JWT; roster check in user-service) — finalized ratings only in listings ---

	ReportDTO getSchoolPortalReport(String byEmail, String forEmail, String token, LocalDate weekStart, LocalDate weekEnd)
			throws ReportException, Exception;

	List<String> getSchoolPortalYearsContainingReports(String byEmail, String forEmail, String token) throws ReportException, Exception;

	List<String> getSchoolPortalMonthsContainingReports(String byEmail, String forEmail, String token, Integer year)
			throws ReportException, Exception;

	List<String> getSchoolPortalWeeksContainingReports(String byEmail, String forEmail, String token, Integer year, String month)
			throws ReportException, Exception;

}
