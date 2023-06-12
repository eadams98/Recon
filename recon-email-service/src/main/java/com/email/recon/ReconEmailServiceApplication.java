package com.email.recon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.EurekaClient;

@SpringBootApplication
public class ReconEmailServiceApplication {
	
	@Autowired
    @Lazy
    private EurekaClient eurekaClient;
	
	@Value("users")
    private String appName;

	public static void main(String[] args) {
		SpringApplication.run(ReconEmailServiceApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}

}
