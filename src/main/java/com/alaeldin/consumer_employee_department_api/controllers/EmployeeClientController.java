package com.alaeldin.consumer_employee_department_api.controllers;

import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import com.alaeldin.consumer_employee_department_api.service.impl.EmployeeClientClientServiceImpl;
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
@RequestMapping("api/v1/consume/employee")
@RequiredArgsConstructor
public class EmployeeClientController
{
    private final EmployeeClientClientServiceImpl employeeClientClientService;
    @PostMapping()
    public ResponseEntity <Mono<String>> saveEmployeeViaClient(@RequestBody @Valid
                                                                   EmployeeDto employeeDto)
    {
        return new ResponseEntity<>(employeeClientClientService
                   .saveEmployee(employeeDto), HttpStatus.OK);
    }

    @GetMapping("/search-employees")
    public ResponseEntity <Mono<PagedModel<EntityModel<EmployeeDto>>>> searchEmployees(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return new ResponseEntity<>(employeeClientClientService.searchEmployees(firstName
                ,lastName,email,page,size),HttpStatus.OK);
    }

    @GetMapping("/get-all-employee")
    public ResponseEntity<Mono<PagedModel<EntityModel<EmployeeDto>>>> getAllEmployees(
            @RequestParam(value = "number", defaultValue = "0") int number,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return new ResponseEntity<>(employeeClientClientService
                             .getAllEmployees(number,size),HttpStatus.OK);
    }

    @GetMapping("get-employee-by-id/{id}")
    public ResponseEntity<Mono<EmployeeDto>> getEmployeeById(@PathVariable("id") long id)
    {

        return new ResponseEntity<>(employeeClientClientService.getEmployeeById(id)
                ,HttpStatus.OK);
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable("id") long id)
    {

        employeeClientClientService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<Mono<String>> updateEmployee(@RequestBody EmployeeDto employeeDto,
                                                       @PathVariable("id") long id)
    {
        employeeDto.setId(id);

        return new ResponseEntity<>(employeeClientClientService.updateEmployee(employeeDto)
                , HttpStatus.OK);
    }
}
