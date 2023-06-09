package com.idea.recon.exception;

import org.springframework.http.HttpStatus;

public class MicroserviceException extends Exception {

	private static final long serialVersionUID = 1L;
	private HttpStatus statusCode;
	
	public MicroserviceException(String msg, HttpStatus statusCode) {
		super(msg);
		this.statusCode = statusCode;
		
	}

	public HttpStatus getStatusCode() {
		return this.statusCode;
	}

}
