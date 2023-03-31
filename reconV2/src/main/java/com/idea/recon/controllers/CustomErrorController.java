package com.idea.recon.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idea.recon.utilities.ErrorInfo;

//@RestController
public class CustomErrorController {
	
	/*private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    
    private static final String PATH = "/error";
    
    @RequestMapping(PATH)
    public ResponseEntity<ErrorInfo> handleError(final HttpServletRequest request,
            final HttpServletResponse response) throws Throwable {
    	logger.warn("Custom error hit");
        throw (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    
    public String getErrorPath() {
        return PATH;
    }*/
}