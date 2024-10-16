package com.alaeldin.consumer_employee_department_api.service;

import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import reactor.core.publisher.Mono;


public interface EmployeeClientService
{
    Mono<String> saveEmployee(EmployeeDto employeeDto);
    Mono<PagedModel<EntityModel<EmployeeDto>>> searchEmployees(
            String firstName, String lastName, String email, int page, int size);
    Mono<PagedModel<EntityModel<EmployeeDto>>> getAllEmployees(int num, int size);
    void deleteEmployee(long id);
     Mono<EmployeeDto> getEmployeeById(long id);
     Mono<String> updateEmployee(EmployeeDto employeeDto);
}
