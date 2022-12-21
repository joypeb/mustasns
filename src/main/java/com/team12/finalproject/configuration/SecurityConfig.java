package com.team12.finalproject.configuration;

import com.team12.finalproject.repository.UserRepository;
import com.team12.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**","/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts","/api/v1/posts/**").permitAll()// join, login은 언제나 가능
                .antMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/v1/posts/**").authenticated()
                .antMatchers(HttpMethod.POST,"/api/v1/users/**/role/change").hasRole("ADMIN")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, userRepository, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}