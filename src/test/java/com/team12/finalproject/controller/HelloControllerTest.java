package com.team12.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.service.AlgorithmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
@MockBean(JpaMetamodelMappingContext.class)
class HelloControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlgorithmService algorithmService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("자릿수 합 구하기 성공")
    @WithMockUser
    public void sumOfDigit_s() throws Exception {
        when(algorithmService.sumOfDigit(1L))
                .thenReturn("1");

        mockMvc.perform(get("/api/v1/hello/{num}",1)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("자릿수 합 구하기 실패 - 숫자 이외의 값")
    @WithMockUser
    public void sumOfDigit_f() throws Exception {
        when(algorithmService.sumOfDigit(anyLong()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST,"숫자 이외의 값"));

        mockMvc.perform(get("/api/v1/hello/{num}","d")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}