package com.team12.finalproject.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {

    //숫자의 각 자릿수의 합을 더해서 출력
    public String sumOfDigit(long num) {
        int result = 0;

        while(num != 0) {
            result += num%10;
            num/=10;
        }

        return String.format("%d",result);
    }
}
