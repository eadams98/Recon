package com.idea.recon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
//@EnableSwagger2
//@OpenAPIDefinition
public class ReconV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ReconV2Application.class, args);
	}

}
