package com.alaeldin.consumer_employee_department_api.service.impl;

import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import com.alaeldin.consumer_employee_department_api.service.EmployeeClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class EmployeeClientClientServiceImpl implements EmployeeClientService
{
    private final WebClient webClient;
    @Override
    public Mono<String> saveEmployee(EmployeeDto employeeDto)
    {

          String url = "employee";

         return webClient.post().uri(url)
               .body(Mono.just(employeeDto),EmployeeDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(ex->
                        System.err.println("Error response: " + ex.getMessage()));
    }

    @Override
    public Mono<PagedModel<EntityModel<EmployeeDto>>> searchEmployees( String firstName
                                                                        , String lastName
                                                                        , String email
                                                                         , int page, int size)
    {

       String url = "/employee/search?firstName={firstName}&lastName={lastName}&email={email}&page={page}&size={size}";

        return webClient
                .get()
                .uri(url, firstName, lastName, email, page, size)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Mono<PagedModel<EntityModel<EmployeeDto>>> getAllEmployees(int num, int size)
    {

        String url ="/employee/get-all-employee?number={number}&size={size}";

        return webClient.get()
                .uri(url, num, size)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedModel<EntityModel<EmployeeDto>>>() {})
                .doOnNext(pagedModel -> System.out.println("Received PagedModel: "
                                                            + pagedModel.getContent()))
                .doOnError(error -> System.err.println("Error occurred: "
                                                       + error.getMessage()));
    }

    @Override
    public void deleteEmployee(long id)
    {

           String url = "/employee/delete/{id}";

           webClient.delete()
                    .uri(url, id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response
                            -> Mono.error(new RuntimeException("Error occurred")))
                    .bodyToMono(String.class)
                    .block();
         }

    @Override
    public Mono<EmployeeDto> getEmployeeById(long id)
    {

        String url = "/employee/get_employee_by_id/{id}";

        return webClient.get()
                .uri(url,id)
                .retrieve()
                .bodyToMono(EmployeeDto.class);
    }

    @Override
    public Mono<String> updateEmployee(EmployeeDto employeeDto)
    {

        String url = "/employee/update/{id}";

        return webClient.post()
                .uri(url, employeeDto.getId())
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(ex -> System.err.println("Error response: " + ex.getMessage()));
    }

}
