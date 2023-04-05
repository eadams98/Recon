package com.discovery.recon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ReconDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReconDiscoveryServiceApplication.class, args);
	}

}
