package com.alaeldin.consumer_employee_department_api.controller;

import com.alaeldin.consumer_employee_department_api.controllers.DepartmentController;
import com.alaeldin.consumer_employee_department_api.dto.DepartmentDto;
import com.alaeldin.consumer_employee_department_api.exception.ResourceNotFoundException;
import com.alaeldin.consumer_employee_department_api.service.impl.DepartmentClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
public class DepartmentClientControllerTest
{
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    public DepartmentClientService departmentClientService;
    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp()
    {

         departmentDto = new DepartmentDto();
         departmentDto.setName("It");
         departmentDto.setId(1L);
    }

    @Test
    void saveDepartmentViaClient_success() throws Exception
    {

        when(departmentClientService.saveDepartment(any(DepartmentDto.class)))
                .thenReturn(Mono.just("Department saved successfully"));

        String employeeJson = "{\"name\":\"it\"}";

        mockMvc.perform(post("/api/v1/consume/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                        .andExpect(status().isOk());
    }

    @Test
    void getAllEmployees_success() throws Exception
    {
        Mono<PagedModel<EntityModel<DepartmentDto>>> mockedResponse = Mono.just(PagedModel.of(
                Collections.singletonList(EntityModel.of(departmentDto)),
                new PagedModel.PageMetadata(1, 0, 1)
        ));

        when(departmentClientService.getAllDepartment(anyInt(), anyInt()))
                        .thenReturn(mockedResponse);

        mockMvc.perform(get("/api/v1/consume/department/get-all-department")
                        .param("number", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    void testGetDepartmentById_success() throws Exception
    {
        long departmentId = 1L;
        when(departmentClientService.getDepartmentById(departmentId))
                .thenReturn(Mono.just(departmentDto));

        mockMvc.perform(get("/api/v1/consume/department/get-department-by-id/{id}"
                , departmentId)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(departmentClientService, times(1))
                .getDepartmentById(departmentId);
    }

    @Test
    void testGetDepartmentById_notFound() throws Exception
    {

        long departmentId = 1L;
        when(departmentClientService.getDepartmentById(departmentId))
                .thenThrow(new ResourceNotFoundException("Department", "id", departmentId));

        mockMvc.perform(get("/api/v1/consume/department/get-department-by-id/{id}"
                        , departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(departmentClientService, times(1))
                .getDepartmentById(departmentId);
    }

    @Test
    public void testDeleteDepartment_Success() throws Exception
    {
        long departmentId = 4L;

        doNothing().when(departmentClientService).deleteDepartment(departmentId);

        mockMvc.perform(get("/api/v1/consume/department/delete/{id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(departmentClientService, times(1))
                .deleteDepartment(departmentId);
    }

    @Test
    void updateEmployee_success() throws Exception
    {

        long departmentId = 4L;

        when(departmentClientService.updateDepartment(any(DepartmentDto.class)))
                .thenReturn(Mono.just("Update Successful"));

        mockMvc.perform(post("/api/v1/consume/department/update/{id}"
                        , departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Hr\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
