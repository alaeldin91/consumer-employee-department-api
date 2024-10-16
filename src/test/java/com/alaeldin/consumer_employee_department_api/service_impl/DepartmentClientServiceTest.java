package com.alaeldin.consumer_employee_department_api.service_impl;

import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import com.alaeldin.consumer_employee_department_api.service.impl.DepartmentClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DepartmentClientServiceTest
{

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private DepartmentClientService departmentClientService;

    public DepartmentClientServiceTest()
    {
    }

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testSaveDepartment_success()
    {

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName("IT");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/department"))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(DepartmentDto.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Success"));

        Mono<String> result = departmentClientService.saveDepartment(departmentDto);

        assertEquals("Success", result.block());

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(eq("/department"));
        verify(requestBodySpec, times(1)).body(any(Mono.class)
                , eq(DepartmentDto.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    void getAllDepartment_success()
    {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setName("Hr");

        PagedModel<EntityModel<DepartmentDto>> pagedModel = PagedModel.of(
                Collections.singletonList(EntityModel.of(departmentDto)),
                new PagedModel.PageMetadata(10, 0, 1)
        );

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(pagedModel));
    }

    @Test
    void testGetDepartmentById_Success()
    {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        long departmentId = 1L;
        DepartmentDto expectedDepartment = new DepartmentDto();
        expectedDepartment.setId(departmentId);
        expectedDepartment.setName("Financial");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/department/get_department_by_id/{id}")
                , eq(departmentId)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DepartmentDto.class))
                .thenReturn(Mono.just(expectedDepartment));

        DepartmentDto actualDepartment = departmentClientService
                .getDepartmentById(departmentId).block();

        assertNotNull(actualDepartment);
        assertEquals(expectedDepartment.getId(), actualDepartment.getId());
        assertEquals(expectedDepartment.getName(), actualDepartment.getName());

        verify(webClient.get()).uri(eq("/department/get_department_by_id/{id}"),
                eq(departmentId));
        verify(requestHeadersSpec).retrieve();
    }

    @Test
    void testGetDepartmentById_NotFound()
    {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        long departmentId = 999L;
        when(requestHeadersUriSpec.uri(eq("/department/get_department_by_id/{id}")
                , eq(departmentId)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DepartmentDto.class)).thenReturn(Mono.empty());

        DepartmentDto actualDepartment = departmentClientService.getDepartmentById(departmentId)
                .block();

        assertNull(actualDepartment);
        verify(webClient.get()).uri(eq("/department/get_department_by_id/{id}")
                , eq(departmentId));
        verify(requestHeadersSpec).retrieve();
    }

    @Test
    public void testDeleteDepartment_Success()
    {

        long departmentId = 4L;

        doReturn(requestHeadersUriSpec).when(webClient).delete();
        doReturn(requestHeadersUriSpec).when(requestHeadersUriSpec).uri(anyString(), anyLong());
        doReturn(responseSpec).when(requestHeadersUriSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(String.class);

        departmentClientService.deleteDepartment(departmentId);

        verify(requestHeadersUriSpec).uri("/department/delete/{id}", departmentId);
    }

    @Test
    public void testUpdateDepartment_Success()
    {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(4L);
        departmentDto.setName("Updated Name");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/department/update/{id}"), eq(4L)))
                .thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(DepartmentDto.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Department updated successfully!"));

        Mono<String> result = departmentClientService.updateDepartment(departmentDto);

        StepVerifier.create(result)
                .expectNext("Department updated successfully!")
                .verifyComplete();
    }


}
