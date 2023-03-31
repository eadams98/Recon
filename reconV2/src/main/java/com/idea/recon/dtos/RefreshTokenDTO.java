package com.idea.recon.dtos;

import java.time.Instant;

import com.idea.recon.entities.Trainee;

import lombok.Data;

@Data
public class RefreshTokenDTO {
	private Integer id;
	private Trainee trainee;
	private String token;
  	private Instant expiryDate;

}
