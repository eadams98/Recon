package com.idea.recon.exceptions;

public class JwtTokenValidationException extends RuntimeException {//RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JwtTokenValidationException(String message) {
        super(message);
    }

}
