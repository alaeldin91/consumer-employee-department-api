package com.alaeldin.consumer_employee_department_api.service;

import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import reactor.core.publisher.Mono;

public interface DepartmentService
{
    Mono<String> saveDepartment(DepartmentDto departmentDto);
    Mono<PagedModel<EntityModel<DepartmentDto>>> searchDepartment(
            String name,  int page, int size);
    Mono<PagedModel<EntityModel<DepartmentDto>>> getAllDepartment(int num, int size);
    void deleteDepartment(long id);
   Mono<DepartmentDto> getDepartmentById(long id);
    Mono<String> updateDepartment(DepartmentDto departmentDto);
}
