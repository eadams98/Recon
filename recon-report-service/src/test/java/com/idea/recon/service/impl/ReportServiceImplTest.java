package com.idea.recon.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.idea.recon.dto.RelationshipVerificationDTO;
import com.idea.recon.dto.ReportDTO;
import com.idea.recon.entity.Report;
import com.idea.recon.enums.Grade;
import com.idea.recon.repository.ReportRepository;
import com.idea.recon.utility.MicroserviceUtil;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private MicroserviceUtil microserviceUtil;

	@Mock
	private ReportRepository reportRepository;

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

		when(restTemplate.exchange(contains("/verify/contractor-to-trainee"), eq(HttpMethod.GET), any(HttpEntity.class),
				eq(RelationshipVerificationDTO.class))).thenReturn(ResponseEntity.ok(relationship));
		when(reportRepository.contractorReportExist(eq(10), eq(20), eq(LocalDate.of(2026, 5, 4)),
				eq(LocalDate.of(2026, 5, 10)))).thenReturn(false);
		when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
			Report report = invocation.getArgument(0);
			report.setReportId(99);
			return report;
		});
		when(restTemplate.exchange(contains("/email/report-created"), eq(HttpMethod.POST), any(HttpEntity.class),
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
}
