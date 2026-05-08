package com.idea.recon.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.idea.recon.entity.Report;
import com.idea.recon.entity.Retort;
import com.idea.recon.enums.Grade;
import com.idea.recon.repository.ReportRepository;
import com.idea.recon.repository.RetortRepository;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
		"spring.jpa.hibernate.ddl-auto=create-drop",
})
class ReportRetortVisibilityIntegrationTest {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private RetortRepository retortRepository;

	@Test
	void contractorDraft_schoolListingExcludesUntilFinalized_juniorCanAddRetort() {
		Integer contractorId = 301;
		Integer traineeId = 401;
		LocalDate weekStart = LocalDate.of(2026, 6, 1);
		LocalDate weekEnd = LocalDate.of(2026, 6, 7);

		Report draft = reportRepository.save(Report.builder()
				.title("Draft weekly")
				.description("Saved by contractor")
				.grade(Grade.B)
				.submissionDate(LocalDate.of(2026, 6, 7))
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.contractorLinkId(contractorId)
				.traineeLinkId(traineeId)
				.isFinalized(false)
				.finalizedAt(null)
				.build());

		assertThat(draft.getReportId()).isNotNull();
		assertThat(draft.getIsFinalized()).isFalse();

		int year = 2026;
		int month = 6;

		List<String> contractorWeeks =
				reportRepository.getWeeksWithReportsOfContractorWithTrainee(contractorId, traineeId, year, month);
		List<String> schoolWeeks =
				reportRepository.getSchoolVisibleWeeksWithReportsOfContractorWithTrainee(contractorId, traineeId, year, month);

		assertThat(contractorWeeks).isNotEmpty();
		assertThat(schoolWeeks).isEmpty();

		draft.setIsFinalized(true);
		draft.setFinalizedAt(LocalDateTime.of(2026, 6, 7, 9, 0));
		reportRepository.saveAndFlush(draft);

		schoolWeeks = reportRepository.getSchoolVisibleWeeksWithReportsOfContractorWithTrainee(contractorId, traineeId, year, month);
		assertThat(schoolWeeks).containsExactlyInAnyOrderElementsOf(contractorWeeks);

		Report finalized = reportRepository.findById(draft.getReportId()).orElseThrow();

		retortRepository.save(Retort.builder()
				.traineeAuthorId(902)
				.content("Junior retort after finalize")
				.createdAt(LocalDateTime.of(2026, 6, 7, 10, 30))
				.report(finalized)
				.build());
		retortRepository.flush();

		assertThat(retortRepository.findByReport_ReportId(finalized.getReportId()))
				.hasValueSatisfying(r -> assertThat(r.getContent()).isEqualTo("Junior retort after finalize"));
	}

	@Test
	void reportAllowsAtMostOneRetort_enforcedByDatabase() {
		LocalDate weekStart = LocalDate.of(2026, 5, 4);
		LocalDate weekEnd = LocalDate.of(2026, 5, 10);
		LocalDateTime finalizedAt = LocalDateTime.of(2026, 5, 7, 15, 0);

		Report finalizedReport = reportRepository.save(Report.builder()
				.title("Weekly")
				.description("Work done")
				.grade(Grade.B)
				.submissionDate(LocalDate.of(2026, 5, 7))
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.contractorLinkId(100)
				.traineeLinkId(200)
				.isFinalized(true)
				.finalizedAt(finalizedAt)
				.build());

		Retort first = Retort.builder()
				.traineeAuthorId(555)
				.content("Junior feedback")
				.createdAt(LocalDateTime.of(2026, 5, 7, 16, 0))
				.report(finalizedReport)
				.build();

		retortRepository.save(first);
		retortRepository.flush();

		assertThat(retortRepository.findByReport_ReportId(finalizedReport.getReportId())).isPresent();

		Retort duplicateRetort = Retort.builder()
				.traineeAuthorId(556)
				.content("Attempted second retort")
				.createdAt(LocalDateTime.of(2026, 5, 8, 10, 0))
				.report(finalizedReport)
				.build();

		assertThatThrownBy(() -> {
			retortRepository.save(duplicateRetort);
			retortRepository.flush();
		}).isInstanceOf(DataIntegrityViolationException.class);
	}
}
