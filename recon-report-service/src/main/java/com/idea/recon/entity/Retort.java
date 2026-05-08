package com.idea.recon.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retort")
public class Retort {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer retortId;

	/** Trainee / junior contractor authoring the retort (minimal FK-style reference). */
	private Integer traineeAuthorId;

	private String content;

	private LocalDateTime createdAt;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(optional = false)
	@JoinColumn(name = "report_id", unique = true, nullable = false)
	private Report report;
}
