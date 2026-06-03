package com.idea.recon.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class InterServiceHttpClientTest {

	@Mock
	private RestTemplate restTemplate;

	private InterServiceHttpClient client;

	@BeforeEach
	void setUp() {
		RetryTemplate retryTemplate = RetryTemplate.builder()
				.maxAttempts(3)
				.fixedBackoff(200)
				.retryOn(ResourceAccessException.class)
				.retryOn(org.springframework.web.client.HttpServerErrorException.class)
				.build();
		client = new InterServiceHttpClient(restTemplate, retryTemplate);
	}

	@Test
	void get_doesNotRetryOn4xx() {
		when(restTemplate.exchange(eq("http://user-service/verify"), eq(HttpMethod.GET), any(HttpEntity.class),
				eq(String.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

		assertThrows(HttpClientErrorException.class,
				() -> client.get("http://user-service/verify", HttpEntity.EMPTY, String.class));

		verify(restTemplate, times(1)).exchange(eq("http://user-service/verify"), eq(HttpMethod.GET),
				any(HttpEntity.class), eq(String.class));
	}

	@Test
	void get_retriesResourceAccessExceptionThenSucceeds() {
		when(restTemplate.exchange(eq("http://user-service/verify"), eq(HttpMethod.GET), any(HttpEntity.class),
				eq(String.class)))
				.thenThrow(new ResourceAccessException("connection reset"))
				.thenReturn(ResponseEntity.ok("ok"));

		ResponseEntity<String> response = client.get("http://user-service/verify", HttpEntity.EMPTY, String.class);

		assertEquals("ok", response.getBody());
		verify(restTemplate, times(2)).exchange(eq("http://user-service/verify"), eq(HttpMethod.GET),
				any(HttpEntity.class), eq(String.class));
	}

	@Test
	void post_doesNotRetryOnTransientFailure() {
		when(restTemplate.exchange(eq("http://email-service/email/report-created"), eq(HttpMethod.POST),
				any(HttpEntity.class), eq(String.class)))
				.thenThrow(new ResourceAccessException("connection reset"));

		assertThrows(ResourceAccessException.class,
				() -> client.postWithoutRetry("http://email-service/email/report-created", HttpEntity.EMPTY,
						String.class));

		verify(restTemplate, times(1)).exchange(eq("http://email-service/email/report-created"),
				eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
	}
}
