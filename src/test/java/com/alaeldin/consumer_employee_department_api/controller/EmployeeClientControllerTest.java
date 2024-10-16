package com.alaeldin.consumer_employee_department_api.controller;

import com.alaeldin.consumer_employee_department_api.controllers.EmployeeClientController;
import com.alaeldin.consumer_employee_department_api.dto.EmployeeDto;
import com.alaeldin.consumer_employee_department_api.exception.ResourceNotFoundException;
import com.alaeldin.consumer_employee_department_api.service.impl.EmployeeClientClientServiceImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeClientController.class)
class EmployeeClientControllerTest {

    @Autowired
    public MockMvc mockMvc;
    @MockBean
    public EmployeeClientClientServiceImpl employeeClientClientService;

    @BeforeEach
    void setUp() {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Alaeldin");
        employeeDto.setLastName("Musa");
        employeeDto.setEmail("alaeldinmus91@gmail.com");
    }

    @Test
    void saveEmployeeViaClient_success() throws Exception
    {

        when(employeeClientClientService.saveEmployee(any(EmployeeDto.class)))
                .thenReturn(Mono.just("Employee saved successfully"));
        String employeeJson = "{\"firstName\":\"Alaeldin\",\"lastName\":\"Musa\"" +
                ",\"email\":\"alaeldin@example.com\"}";
        mockMvc.perform(post("/api/v1/consume/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                        .andExpect(status().isOk());
    }

    @Test
    void getAllEmployees_success() throws Exception
    {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("Alaeldin");
        employeeDto.setLastName("Musa");
        employeeDto.setEmail("alaeldinmusa91@gmail.com");
        employeeDto.setContactNumber(null);
        employeeDto.setDepartmentId(1L);

        Mono<PagedModel<EntityModel<EmployeeDto>>> mockedResponse = Mono.just(PagedModel.of(
                Collections.singletonList(EntityModel.of(employeeDto)),
                new PagedModel.PageMetadata(1, 0, 1)
        ));

        when(employeeClientClientService.getAllEmployees(anyInt(), anyInt())).thenReturn(mockedResponse);

        mockMvc.perform(get("/api/v1/consume/employee/get-all-employee")
                        .param("number", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testGetEmployeeById_success() throws Exception
    {
        long employeeId = 1L;
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employeeId);
        employeeDto.setFirstName("Alaeldin");
        employeeDto.setLastName("Musa");

        when(employeeClientClientService.getEmployeeById(employeeId))
                .thenReturn(Mono.just(employeeDto));

        mockMvc.perform(get("/api/v1/consume/employee/get-employee-by-id/{id}"
                , employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        verify(employeeClientClientService, times(1))
                .getEmployeeById(employeeId);
    }

    @Test
    void testGetEmployeeById_notFound() throws Exception
    {

        long employeeId = 1L;

        when(employeeClientClientService.getEmployeeById(employeeId))
                .thenThrow(new ResourceNotFoundException("Employee", "id", employeeId));

        mockMvc.perform(get("/api/v1/consume/employee/get-employee-by-id/{id}"
                        , employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(employeeClientClientService, times(1))
                .getEmployeeById(employeeId);
    }

    @Test
    public void testDeleteEmployee_Success() throws Exception
    {

        long employeeId = 4L;

        doNothing().when(employeeClientClientService).deleteEmployee(employeeId);

        mockMvc.perform(get("/api/v1/consume/employee/delete/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeClientClientService, times(1))
                .deleteEmployee(employeeId);
    }

    @Test
    void updateEmployee_success() throws Exception {

        long employeeId = 4L;
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Haji");
        employeeDto.setLastName("Musa");
        employeeDto.setEmail("alaeldinmusa91@gmail.com");
        employeeDto.setId(employeeId);

        when(employeeClientClientService.updateEmployee(any(EmployeeDto.class)))
                .thenReturn(Mono.just("Update Successful"));

        mockMvc.perform(post("/api/v1/consume/employee/update/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Haji\", \"lastName\": \"Musa\"" +
                                ", \"email\": \"alaeldinmusa91@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
