package com.idea.recon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "recon.inter-service.http")
public class InterServiceHttpProperties {

	private int connectTimeoutMs = 2000;
	private int readTimeoutMs = 5000;
	private Retry retry = new Retry();

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}

	public Retry getRetry() {
		return retry;
	}

	public void setRetry(Retry retry) {
		this.retry = retry;
	}

	public static class Retry {

		private int maxAttempts = 3;
		private long backoffMs = 300;

		public int getMaxAttempts() {
			return maxAttempts;
		}

		public void setMaxAttempts(int maxAttempts) {
			this.maxAttempts = maxAttempts;
		}

		public long getBackoffMs() {
			return backoffMs;
		}

		public void setBackoffMs(long backoffMs) {
			this.backoffMs = backoffMs;
		}
	}
}
