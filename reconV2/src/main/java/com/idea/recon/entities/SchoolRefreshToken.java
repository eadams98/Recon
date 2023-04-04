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

@Entity(name = "school_refresh_token")
@Data
public class SchoolRefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	//@JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id")
	@JoinColumn(name = "school_id")
	private School school;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
  	private Instant expiryDate;
	
}
