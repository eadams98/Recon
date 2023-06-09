package com.idea.recon.utility;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorInfo {
	private String errorMessage;
	private Integer errorCode;
	private LocalDateTime timestamp;
}
