package com.idea.recon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;

//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSStorageConfig {
	
	String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
	String accessSecret = System.getenv("AWS_SECRET_ACCESS_KEY");
	String region = "us-east-2";
	
	/*@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String accessSecret;
	
	@Value("${cloud.aws.region.static}")
	private String region;*/

	@Bean
	public AmazonS3 generateS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
		
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
	}
	
	/*
	@Bean
	public S3Client generateS3Client() {
	S3Client s3Client = S3Client.builder()
	        .region(Region.US_EAST_1) // Replace with your desired region
	        .credentialsProvider(DefaultCredentialsProvider.create())
	        .build();
	return s3Client;
	}*/
	
}
