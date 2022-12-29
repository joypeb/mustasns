package com.team12.finalproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AlgorithmServiceTest {

    AlgorithmService algorithmService;

    @BeforeEach
    void before() {
        algorithmService = new AlgorithmService();
    }


    @Test
    @DisplayName("자릿수 구하는 로직 동작 성공")
    void sumOfDigit_s() {
        assertEquals("10",algorithmService.sumOfDigit(55));
        assertEquals("11",algorithmService.sumOfDigit(56));
    }

}