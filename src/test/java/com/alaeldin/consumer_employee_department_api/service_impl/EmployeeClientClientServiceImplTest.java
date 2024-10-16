package com.alaeldin.consumer_employee_department_api.service_impl;

import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import com.alaeldin.consumer_employee_department_api.service.impl.EmployeeClientClientServiceImpl;
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
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class EmployeeClientClientServiceImplTest

{
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private EmployeeClientClientServiceImpl employeeClientService;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriMock;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployee_success() {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Alaeldin");
        employeeDto.setLastName("Musa");
        employeeDto.setEmail("alaeldin@example.com");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("employee"))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(EmployeeDto.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Success"));

        Mono<String> result = employeeClientService.saveEmployee(employeeDto);

        assertEquals("Success", result.block());

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(eq("employee"));
        verify(requestBodySpec, times(1)).body(any(Mono.class)
                , eq(EmployeeDto.class));
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
    void getEmployeeById_success()
    {

        long employeeId = 1L;
        EmployeeDto expectedEmployee = new EmployeeDto();
        expectedEmployee.setId(employeeId);
        expectedEmployee.setFirstName("John");
        expectedEmployee.setLastName("Doe");
        expectedEmployee.setEmail("john.doe@example.com");

    }

    @Test
    public void testDeleteEmployee_Success()
    {
        long employeeId = 4L;

        doReturn(requestHeadersUriMock).when(webClient).delete();
        doReturn(requestHeadersUriMock).when(requestHeadersUriMock).uri(anyString(), anyLong());
        doReturn(responseSpec).when(requestHeadersUriMock).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(String.class);

        employeeClientService.deleteEmployee(employeeId);

        verify(requestHeadersUriMock).uri("/employee/delete/{id}", employeeId);
    }

    @Test
    public void testUpdateEmployee_Success()
    {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(4L);
        employeeDto.setFirstName("Updated Name");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/employee/update/{id}"), eq(4L)))
                                                           .thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(EmployeeDto.class)))
                                           .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.empty());
        Mono<String> result = employeeClientService.updateEmployee(employeeDto);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testEmployeeById_Success()
    {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        long employeeId = 1L;
        EmployeeDto expectedEmployment = new EmployeeDto();
        expectedEmployment.setId(employeeId);
        expectedEmployment.setFirstName("Alaeldin");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/employee/get_employee_by_id/{id}"),
                eq(employeeId)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(EmployeeDto.class))
                .thenReturn(Mono.just(expectedEmployment));

        Mono<EmployeeDto> actualEmployment = employeeClientService.getEmployeeById(employeeId);

        assertNotNull(actualEmployment);
        assertEquals(expectedEmployment.getId(), Objects.requireNonNull(actualEmployment.block())
                .getId());
        assertEquals(expectedEmployment.getFirstName(), Objects.requireNonNull(actualEmployment
                .block()).getFirstName());

        verify(webClient.get()).uri(eq("/employee/get_employee_by_id/{id}"), eq(employeeId));
        verify(requestHeadersSpec).retrieve();
    }

    @Test
    void testGetEmploymentById_NotFound()
    {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        long employeeId = 999L;
        when(requestHeadersUriSpec.uri(eq("/employee/get_employee_by_id/{id}")
                , eq(employeeId)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(EmployeeDto.class)).thenReturn(Mono.empty());

       EmployeeDto actualDepartment = employeeClientService.getEmployeeById(employeeId)
                .block();

        assertNull(actualDepartment);
        verify(webClient.get()).uri(eq("/employee/get_employee_by_id/{id}")
                , eq(employeeId));
        verify(requestHeadersSpec).retrieve();
    }

}
