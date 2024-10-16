package com.alaeldin.consumer_employee_department_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
   //BaseUrl EmployeeDepartmentApi http://localhost:8080/api/v1
    private String baseUrl;
}
