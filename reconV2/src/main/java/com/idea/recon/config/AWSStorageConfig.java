package com.idea.recon.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSStorageConfig {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean
	public AmazonS3 generateS3Client() {
		String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
		String accessSecret = System.getenv("AWS_SECRET_ACCESS_KEY");
		String region = System.getenv().getOrDefault("AWS_REGION", "us-east-2");
		if (accessKey == null || accessSecret == null) {
			throw new IllegalStateException(
					"Set AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY (or use instance profile / default provider in a future refactor).");
		}
		logger.info("S3 client using region: {}", region);
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);

		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
	}

}
