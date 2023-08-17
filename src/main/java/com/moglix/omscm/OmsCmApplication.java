package com.moglix.omscm;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OmsCmApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmsCmApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
	            .setConnectTimeout(Duration.ofMillis(20000))
	            .setReadTimeout(Duration.ofMillis(20000))
	            .build();
	}
}
