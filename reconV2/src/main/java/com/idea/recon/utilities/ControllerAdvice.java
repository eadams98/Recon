package com.idea.recon.utilities;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.JwtTokenValidationException;
import com.idea.recon.exceptions.TokenRefreshException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestControllerAdvice
public class ControllerAdvice {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Environment env;


	/*
	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
	  return new ErrorMessage(
	      HttpStatus.FORBIDDEN.value(),
	      new Date(),
	      ex.getMessage(),
	      request.getDescription(false));
	}
  	*/
	
	/*
	@ExceptionHandler
    public ResponseEntity<String> handleAccessDeniedException(ExpiredJwtException e, HttpServletRequest request){
        return ResponseEntity.status(403).body(e.getMessage());
    }
	
	*/
	@ExceptionHandler
    public ResponseEntity<ErrorInfo> handleAccessDeniedException(ExpiredJwtException ex, HttpServletRequest request) {
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(ex.getMessage());
		error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.UNAUTHORIZED);
    }
	 

	/*
	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(ExpiredJwtException exception) {
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(exception.getMessage());
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfo> handleJwtTokenValidationException(RuntimeException ex) {
		if (ex.getCause() instanceof JwtTokenValidationException) {
            // handle JwtTokenValidationException
            JwtTokenValidationException jwtException = (JwtTokenValidationException) ex.getCause();
            //ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, jwtException.getMessage());
            ErrorInfo error = new ErrorInfo();
    		error.setErrorMessage(jwtException.getMessage());
    		error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
    		error.setTimestamp(LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        } else {
            // handle other runtime exceptions
            //ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            ErrorInfo error = new ErrorInfo();
    		error.setErrorMessage("Internal Server Error");
    		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		error.setTimestamp(LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
	
	@ExceptionHandler(ContractorException.class)
    public ResponseEntity<ErrorInfo> handleJwtTokenValidationException(ContractorException ex) {
		logger.info("ADVICE CONTROLLER: ContractorException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(env.getProperty(ex.getMessage()));
		error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(JwtTokenValidationException.class)
    public ResponseEntity<ErrorInfo> handleJwtTokenValidationException(JwtTokenValidationException ex) {
		logger.info("ADVICE CONTROLLER: JwtTokenValidationException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(ex.getMessage());
		error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorInfo> handleMalformedJwtException(MalformedJwtException ex) {
		logger.info("ADVICE CONTROLLER: MalformedJwtException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(ex.getMessage());
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorInfo> handleMalformedJwtException(SignatureException ex) {
		logger.info("ADVICE CONTROLLER: SignatureException");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(ex.getMessage());
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorInfo> handleIOException(IOException ex) {
		logger.info("ADVICE CONTROLLER: IOEXception");
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(ex.getMessage());
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception) {
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(exception.getMessage());
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
