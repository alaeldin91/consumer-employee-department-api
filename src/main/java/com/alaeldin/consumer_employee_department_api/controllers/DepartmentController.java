package com.alaeldin.consumer_employee_department_api.controllers;

import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import com.alaeldin.consumer_employee_department_api.service.impl.DepartmentClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/consume/department")
@RequiredArgsConstructor
public class DepartmentController
{
    //Inject DepartmentClientService
    private final DepartmentClientService departmentClientService;

    /**
     * @param departmentDto departmentDto
     * @return ResponseEntity <Mono<String>
     */
    @PostMapping()
    public ResponseEntity <Mono<String>> saveDepartmentViaClient(@RequestBody @Valid
                                                                 DepartmentDto departmentDto)
    {
      return new ResponseEntity<>(departmentClientService
              .saveDepartment(departmentDto), HttpStatus.OK);
    }
    @GetMapping("/search-department")
    public ResponseEntity <Mono<PagedModel<EntityModel<DepartmentDto>>>> searchEmployees(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return new ResponseEntity<>(departmentClientService
                .searchDepartment(name,page,size)
                ,HttpStatus.OK);
    }
    @GetMapping("/get-all-department")
    public ResponseEntity<Mono<PagedModel<EntityModel<DepartmentDto>>>> getAllDepartment(
            @RequestParam(value = "number", defaultValue = "0") int number,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {

        return new ResponseEntity<>(departmentClientService
                .getAllDepartment(number,size),HttpStatus.OK);
    }
    @GetMapping("get-department-by-id/{id}")
    public ResponseEntity<Mono<DepartmentDto>> getDepartmentById(@PathVariable("id") long id)
    {

        return new ResponseEntity<>(departmentClientService.getDepartmentById(id)
                ,HttpStatus.OK);
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable("id") long id)
    {

        departmentClientService.deleteDepartment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Department deleted successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<Mono<String>> updateDepartment(@RequestBody DepartmentDto departmentDto,
                                                       @PathVariable("id") long id)
    {
        departmentDto.setId(id);

        return new ResponseEntity<>(departmentClientService.updateDepartment(departmentDto)
                , HttpStatus.OK);
    }
}
