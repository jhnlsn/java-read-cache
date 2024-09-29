package com.example.demo.controller;

import com.example.demo.model.Parameter;
import com.example.demo.service.ParameterService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ParameterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParameterService parameterService;

    @Test
    public void testGetParameter() throws Exception {
        Parameter parameter = new Parameter("123", "Test Name");
        Mockito.when(parameterService.getParameterById(anyString())).thenReturn(parameter);

        mockMvc.perform(get("/parameters/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Test Name"));
    }
}
