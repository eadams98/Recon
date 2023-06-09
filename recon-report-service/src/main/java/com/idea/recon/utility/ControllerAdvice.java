package com.idea.recon.utility;

import java.time.LocalDateTime;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idea.recon.exception.MicroserviceException;
import com.idea.recon.exception.ReportException;

@RestControllerAdvice
public class ControllerAdvice {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Environment env;
	
	@ExceptionHandler(value = ReportException.class)
	public ResponseEntity<ErrorInfo> ReportExceptionHandler(ReportException exception) {
		logger.error("ReportException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(env.getProperty(exception.getMessage()));
		error.setErrorCode(HttpStatus.BAD_REQUEST.value()); 
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MicroserviceException.class)
	public ResponseEntity<ErrorInfo> MicroserviceExceptionHandler(MicroserviceException exception) {
		logger.error("MicroserviceException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(exception.getMessage());
		error.setErrorCode(exception.getStatusCode().value()); 
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, exception.getStatusCode());
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception) {
		logger.error("GeneralExceptionHandler");
		ErrorInfo error = new ErrorInfo();
		if(exception.getMessage() == null)
			error.setErrorMessage("INTERNAL ISSUE PLEASE CHECK BACKEND");
		else
			error.setErrorMessage(exception.getMessage());
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
