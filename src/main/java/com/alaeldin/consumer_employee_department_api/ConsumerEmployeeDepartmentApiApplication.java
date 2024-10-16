package com.alaeldin.consumer_employee_department_api;

import com.alaeldin.consumer_employee_department_api.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties.class)
public class ConsumerEmployeeDepartmentApiApplication {

	public static void main(String[] args) {
		SpringApplication
				.run(ConsumerEmployeeDepartmentApiApplication.class, args);
	}
	@Bean
	public WebClient webClient(ApiProperties apiProperties){

		return WebClient.builder().baseUrl(apiProperties.getBaseUrl()).build();
	}
}
