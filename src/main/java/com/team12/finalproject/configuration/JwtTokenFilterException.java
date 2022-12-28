package com.team12.finalproject.configuration;

import com.team12.finalproject.exception.AppException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilterException extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*try {
            //JwtFilter 를 호출하는데, 이 필터에서 jwtTokenNotAvailable 이 떨어진다.
            filterChain.doFilter(request,response);
        } catch (AppException e) {
            
        }*/
    }
}
