package com.idea.recon;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.idea.recon.config.InterServiceHttpProperties;

@SpringBootApplication
@EnableConfigurationProperties(InterServiceHttpProperties.class)
public class ReconReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReconReportServiceApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder, InterServiceHttpProperties httpProperties) {
		return builder
				.setConnectTimeout(Duration.ofMillis(httpProperties.getConnectTimeoutMs()))
				.setReadTimeout(Duration.ofMillis(httpProperties.getReadTimeoutMs()))
				.build();
	}

}
