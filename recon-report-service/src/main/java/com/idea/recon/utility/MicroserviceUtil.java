package com.idea.recon.utility;

import java.io.IOException;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idea.recon.exception.MicroserviceException;

@Component
public class MicroserviceUtil {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void handleHttpClientExceptionAndHttpServerException(Exception ex) throws MicroserviceException {
		
		logger.info(ex.getClass().toString());
		logger.info(ex.getMessage());
		
		if (!(ex instanceof HttpClientErrorException) && !(ex instanceof HttpServerErrorException) )
			return;
		
		if ( ((HttpClientErrorException) ex).getStatusCode().is4xxClientError()) {
	        // Handle 4xx client error
	        String responseBody = ((HttpClientErrorException) ex).getResponseBodyAsString();
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            Map<String, Object> responseMap = mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
	            String errorMessage = (String) responseMap.get("errorMessage");
	            System.out.println("Error response: " + ((HttpClientErrorException) ex).getStatusCode());
	            System.out.println("Error message: " + errorMessage);
	            throw new MicroserviceException(String.valueOf(responseMap.get("errorMessage")), ((HttpClientErrorException) ex).getStatusCode());

	        } catch (IOException exception) {
	            System.out.println("Failed to parse error response: " + responseBody);
	        }
	    }
		
		if (((HttpServerErrorException) ex).getStatusCode().is5xxServerError()) {
	        // Handle 5xx server error
	        String responseBody = ((HttpServerErrorException) ex).getResponseBodyAsString();
	        System.out.println("Error response: " + ((HttpServerErrorException) ex).getStatusCode());
	        System.out.println("Error message: " + responseBody);
	        throw new MicroserviceException(responseBody, ((HttpServerErrorException) ex).getStatusCode());
	    }
	}

}
