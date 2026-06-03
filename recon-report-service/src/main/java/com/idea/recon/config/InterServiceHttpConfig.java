package com.idea.recon.config;

import java.net.SocketTimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Configuration
public class InterServiceHttpConfig {

	@Bean
	public RetryTemplate interServiceRetryTemplate(InterServiceHttpProperties properties) {
		InterServiceHttpProperties.Retry retry = properties.getRetry();
		return RetryTemplate.builder()
				.maxAttempts(retry.getMaxAttempts())
				.fixedBackoff(retry.getBackoffMs())
				.retryOn(ResourceAccessException.class)
				.retryOn(SocketTimeoutException.class)
				.retryOn(HttpServerErrorException.class)
				.build();
	}
}
