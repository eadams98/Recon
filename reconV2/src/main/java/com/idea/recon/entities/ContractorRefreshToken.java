package com.idea.recon.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity(name = "contractor_refresh_token")
@Data
public class ContractorRefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	//@JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id")
	@JoinColumn(name = "contractor_id")
	private Contractor contractor;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
  	private Instant expiryDate;
}
