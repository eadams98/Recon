package com.idea.recon.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Outbound HTTP to peer services (user-service, email-service) with timeouts on the
 * underlying {@link RestTemplate} and retry only for safe, idempotent GET calls.
 */
@Component
public class InterServiceHttpClient {

	private final RestTemplate restTemplate;
	private final RetryTemplate retryTemplate;

	public InterServiceHttpClient(
			RestTemplate restTemplate,
			@Qualifier("interServiceRetryTemplate") RetryTemplate interServiceRetryTemplate) {
		this.restTemplate = restTemplate;
		this.retryTemplate = interServiceRetryTemplate;
	}

	public <T> ResponseEntity<T> get(String url, HttpEntity<?> entity, Class<T> responseType) {
		return retryTemplate.execute(context -> restTemplate.exchange(url, HttpMethod.GET, entity, responseType));
	}

	/**
	 * POST and other side-effecting calls: single attempt only (no retry).
	 */
	public <T> ResponseEntity<T> postWithoutRetry(String url, HttpEntity<?> entity, Class<T> responseType) {
		return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
	}
}
