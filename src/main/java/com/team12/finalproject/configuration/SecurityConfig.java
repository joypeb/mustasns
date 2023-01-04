package com.team12.finalproject.configuration;

import com.team12.finalproject.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()

                //인증 확인
                .authorizeRequests()
                //전부 허가
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**","/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts","/api/v1/posts/**","api/v1/posts/**/comments","api/v1/posts/**/likes").permitAll()// join, login은 언제나 가능
                //post
                .antMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.GET,"api/v1/posts/my").authenticated()
                //comment
                .antMatchers(HttpMethod.POST, "/api/v1/posts/**/comments").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/**/comments/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/v1/posts/**/comments/**").authenticated()
                //like
                .antMatchers(HttpMethod.POST,"/api/v1/posts/**/likes").authenticated()
                //admin
                .antMatchers(HttpMethod.POST,"/api/v1/users/**/role/change").hasRole("ADMIN")
                .and()

                //jwt
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtTokenFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new JwtTokenFilterException(), JwtTokenFilter.class)
                .build();
    }
}