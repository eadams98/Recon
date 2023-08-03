package com.idea.recon.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.idea.recon.entity.Report;


public interface ReportRepository extends JpaRepository<Report, Integer> {
	
	/*@Modifying
	@Query(
		value = "INSERT INTO Report"
				+ " (title, description, grade, submission_date, week_start_date, week_end_date, contractor_link_id, trainee_link_id) "
				+ " VALUES (:report.title, :report.description, :report.grade, :report.submissionDate, :report.weekStartDate, :report.weekEndDate, :contractorId, :traineeId)",
		nativeQuery = true
	)
	Integer ContractorReportSubmission(Report report, Integer contractorId, Integer traineeId);*/

	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
		       "FROM Report r " +
		       "WHERE contractor_link_id = :contractorId AND trainee_link_id = :traineeId AND week_start_date = :startOfWeek AND week_end_date = :endOfWeek")
	Boolean contractorReportExist(Integer contractorId, Integer traineeId, LocalDate startOfWeek, LocalDate endOfWeek);
	
	@Query("SELECT r " +
		       "FROM Report r " +
		       "WHERE contractor_link_id = :contractorId AND trainee_link_id = :traineeId AND week_start_date = :startOfWeek AND week_end_date = :endOfWeek")
	Optional<Report>  getSpecificReport(Integer contractorId, Integer traineeId, LocalDate startOfWeek, LocalDate endOfWeek);
	
	
	@Query("SELECT YEAR(week_start_date) AS year FROM Report " +
			"WHERE contractor_link_id = :contractorId AND trainee_link_id = :traineeId " +
			"GROUP BY YEAR(week_start_date)")
	List<String> getYearsWithReportsOfContractorWithTrainee(Integer contractorId, Integer traineeId);
	
	@Query("SELECT DATE_FORMAT(week_start_date, '%M') AS month FROM Report " +
			"WHERE contractor_link_id = :contractorId AND trainee_link_id = :traineeId AND YEAR(week_start_date) = :year " +
			"GROUP BY month")
	List<String> getMonthsWithReportsOfContractorWithTrainee(Integer contractorId, Integer traineeId, Integer year);
	
	@Query("SELECT CONCAT(week_start_date, ' - ',  week_end_date) AS weekly_report FROM Report " +
			"WHERE contractor_link_id = :contractorId AND trainee_link_id = :traineeId AND YEAR(week_start_date) = :year AND MONTH(week_start_date) = :month"
	)
	List<String> getWeeksWithReportsOfContractorWithTrainee(Integer contractorId, Integer traineeId, Integer year, Integer month);
	
}

