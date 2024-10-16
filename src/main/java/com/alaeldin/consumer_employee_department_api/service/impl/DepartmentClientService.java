package com.alaeldin.consumer_employee_department_api.service.impl;

import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import com.alaeldin.consumer_employee_department_api.service.DepartmentService;
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
public class DepartmentClientService implements DepartmentService
{
    private final WebClient webClient;

    @Override
    public Mono<String> saveDepartment(DepartmentDto departmentDto)
    {
        String url = "/department";

        return webClient.post().uri(url)
                .body(Mono.just(departmentDto),DepartmentDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(ex->
                        System.err.println("Error response: " + ex.getMessage()));
    }
    @Override
    public Mono<PagedModel<EntityModel<DepartmentDto>>> searchDepartment(String name
            , int page, int size)
    {

        String url = "/department/search?name={name}&page={page}&size={size}";

        return webClient
                .get()
                .uri(url, name, page, size)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference
                        <>() {
                });
    }
    @Override
    public Mono<PagedModel<EntityModel<DepartmentDto>>> getAllDepartment(int num, int size)
    {

        String url ="/department/get-all-department?number={number}&size={size}";

        return webClient.get()
                .uri(url, num, size)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedModel<EntityModel<DepartmentDto>>>() {})
                .doOnNext(pagedModel -> System.out.println("Received PagedModel: "
                        + pagedModel.getContent()))
                .doOnError(error -> System.err.println("Error occurred: "
                        + error.getMessage()));

    }

    @Override
    public void deleteDepartment(long id)
    {
        String url = "/department/delete/{id}";

        webClient.delete()
                .uri(url, id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response
                        -> Mono.error(new RuntimeException("Error occurred")))
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Mono<DepartmentDto> getDepartmentById(long id)
    {
        String url = "/department/get_department_by_id/{id}";

        return webClient.get()
                .uri(url,id)
                .retrieve()
                .bodyToMono(DepartmentDto.class);
    }

    @Override
    public Mono<String> updateDepartment(DepartmentDto departmentDto) {
        String url = "/department/update/{id}";

        return webClient.post()
                .uri(url, departmentDto.getId())
                .body(Mono.just(departmentDto), DepartmentDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(ex -> System.err.println("Error response: " + ex.getMessage()));
    }

}
