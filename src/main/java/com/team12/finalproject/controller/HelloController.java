package com.team12.finalproject.controller;

import com.team12.finalproject.service.AlgorithmService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@ApiIgnore
public class HelloController {

    private final AlgorithmService algorithmService;
    @GetMapping("/hello")
    public String hello() {
        return "박은빈";
    }

    @GetMapping("/hello/{num}")
    public String sumOfDigit(@PathVariable long num) {
        return algorithmService.sumOfDigit(num);
    }
}
