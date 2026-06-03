package com.idea.recon.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.idea.recon.client.InterServiceHttpClient;
import com.idea.recon.dto.CreateRetortDTO;
import com.idea.recon.dto.RelationshipVerificationDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.entity.Report;
import com.idea.recon.entity.Retort;
import com.idea.recon.enums.Grade;
import com.idea.recon.exception.ReportException;
import com.idea.recon.repository.ReportRepository;
import com.idea.recon.repository.RetortRepository;
import com.idea.recon.utility.MicroserviceUtil;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

	@Mock
	private InterServiceHttpClient interServiceHttpClient;

	@Mock
	private MicroserviceUtil microserviceUtil;

	@Mock
	private ReportRepository reportRepository;

	@Mock
	private RetortRepository retortRepository;

	@InjectMocks
	private ReportServiceImpl reportService;

	@Test
	void createReportDefaultsToDraftStateForContractorSubmission() throws Exception {
		RelationshipVerificationDTO relationship = RelationshipVerificationDTO.builder()
				.byId(10)
				.forId(20)
				.byName("Contractor Name")
				.forName("Trainee Name")
				.byEmail("contractor@example.com")
				.forEmail("trainee@example.com")
				.build();

		ReportDTO reportDTO = ReportDTO.builder()
				.title("Weekly Report")
				.description("Progress update")
				.grade("A")
				.sentByEmail("contractor@example.com")
				.sentForEmail("trainee@example.com")
				.weekStartDate(LocalDate.of(2026, 5, 4))
				.weekEndDate(LocalDate.of(2026, 5, 10))
				.build();

		when(interServiceHttpClient.get(contains("/verify/contractor-to-trainee"), any(HttpEntity.class),
				eq(RelationshipVerificationDTO.class))).thenReturn(ResponseEntity.ok(relationship));
		when(reportRepository.contractorReportExist(eq(10), eq(20), eq(LocalDate.of(2026, 5, 4)),
				eq(LocalDate.of(2026, 5, 10)))).thenReturn(false);
		when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
			Report report = invocation.getArgument(0);
			report.setReportId(99);
			return report;
		});
		when(interServiceHttpClient.postWithoutRetry(contains("/email/report-created"), any(HttpEntity.class),
				eq(String.class))).thenReturn(ResponseEntity.ok("ok"));

		String result = reportService.createReport(reportDTO, "token");

		ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
		verify(reportRepository).save(reportCaptor.capture());
		Report savedReport = reportCaptor.getValue();

		assertEquals("Report Created.", result);
		assertFalse(savedReport.getIsFinalized());
		assertNull(savedReport.getFinalizedAt());
	}

	@Test
	void toReportDtoIncludesFinalizationMetadata() {
		LocalDateTime finalizedAt = LocalDateTime.of(2026, 5, 7, 12, 30);
		Report report = Report.builder()
				.reportId(7)
				.title("Title")
				.description("Body")
				.grade(Grade.A)
				.isFinalized(true)
				.finalizedAt(finalizedAt)
				.build();

		ReportDTO dto = report.toReportDTO("contractor@example.com", "trainee@example.com");

		assertTrue(dto.getIsFinalized());
		assertEquals(finalizedAt, dto.getFinalizedAt());
	}

	@Test
	void finalizeReport_setsFinalizedFlags() throws Exception {
		RelationshipVerificationDTO relationship = RelationshipVerificationDTO.builder()
				.byId(10)
				.forId(20)
				.build();
		LocalDate weekStart = LocalDate.of(2026, 5, 4);
		LocalDate weekEnd = LocalDate.of(2026, 5, 10);
		Report draft = Report.builder()
				.reportId(5)
				.contractorLinkId(10)
				.traineeLinkId(20)
				.description("d")
				.title("t")
				.grade(Grade.B)
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.isFinalized(false)
				.finalizedAt(null)
				.build();

		when(interServiceHttpClient.get(contains("/verify/contractor-to-trainee"), any(HttpEntity.class),
				eq(RelationshipVerificationDTO.class))).thenReturn(ResponseEntity.ok(relationship));
		when(reportRepository.getSpecificReport(eq(10), eq(20), eq(weekStart), eq(weekEnd)))
				.thenReturn(Optional.of(draft));
		when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ReportDTO result = reportService.finalizeReport("c@x.com", "t@x.com", "jwt", weekStart, weekEnd);

		ArgumentCaptor<Report> captor = ArgumentCaptor.forClass(Report.class);
		verify(reportRepository).save(captor.capture());
		assertTrue(captor.getValue().getIsFinalized());
		assertTrue(result.getIsFinalized());
		assertEquals(weekStart, result.getWeekStartDate());
	}

	@Test
	void createTraineeRetort_requiresFinalizedReport() {
		RelationshipVerificationDTO relationship =
				RelationshipVerificationDTO.builder().byId(10).forId(20).build();
		LocalDate weekStart = LocalDate.of(2026, 5, 4);
		LocalDate weekEnd = LocalDate.of(2026, 5, 10);
		Report draft = Report.builder()
				.reportId(8)
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.isFinalized(false)
				.build();
		CreateRetortDTO dto = CreateRetortDTO.builder()
				.content("Reply")
				.sentByEmail("c@x.com")
				.sentForEmail("t@x.com")
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.build();

		when(interServiceHttpClient.get(contains("/verify/trainee/contractor-to-trainee"),
				any(HttpEntity.class), eq(RelationshipVerificationDTO.class)))
				.thenReturn(ResponseEntity.ok(relationship));
		when(reportRepository.getSpecificReport(eq(10), eq(20), eq(weekStart), eq(weekEnd)))
				.thenReturn(Optional.of(draft));

		assertThrows(ReportException.class, () -> reportService.createTraineeRetort(dto, "jwt"));
	}

	@Test
	void createTraineeRetort_persistsWhenFinalized() throws Exception {
		RelationshipVerificationDTO relationship =
				RelationshipVerificationDTO.builder().byId(10).forId(20).build();
		LocalDate weekStart = LocalDate.of(2026, 5, 4);
		LocalDate weekEnd = LocalDate.of(2026, 5, 10);
		Report finalized = Report.builder()
				.reportId(12)
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.isFinalized(true)
				.finalizedAt(LocalDateTime.of(2026, 5, 5, 9, 0))
				.description("d")
				.title("t")
				.grade(Grade.A)
				.build();
		CreateRetortDTO dto = CreateRetortDTO.builder()
				.content("Junior reply")
				.sentByEmail("c@x.com")
				.sentForEmail("t@x.com")
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.build();

		when(interServiceHttpClient.get(contains("/verify/trainee/contractor-to-trainee"),
				any(HttpEntity.class), eq(RelationshipVerificationDTO.class)))
				.thenReturn(ResponseEntity.ok(relationship));
		when(reportRepository.getSpecificReport(eq(10), eq(20), eq(weekStart), eq(weekEnd)))
				.thenReturn(Optional.of(finalized));
		when(retortRepository.findByReport_ReportId(12)).thenReturn(Optional.empty());
		when(retortRepository.save(any(Retort.class))).thenAnswer(invocation -> {
			Retort r = invocation.getArgument(0);
			return r;
		});
		Retort attached = Retort.builder().content("Junior reply").traineeAuthorId(20).report(finalized).build();
		Report withRetort = Report.builder()
				.reportId(12)
				.weekStartDate(weekStart)
				.weekEndDate(weekEnd)
				.isFinalized(true)
				.finalizedAt(finalized.getFinalizedAt())
				.description("d")
				.title("t")
				.grade(Grade.A)
				.retort(attached)
				.build();
		when(reportRepository.findById(12)).thenReturn(Optional.of(withRetort));

		ReportDTO out = reportService.createTraineeRetort(dto, "jwt");

		assertEquals("Junior reply", out.getRetortContent());
		verify(retortRepository).save(any(Retort.class));
	}
}
