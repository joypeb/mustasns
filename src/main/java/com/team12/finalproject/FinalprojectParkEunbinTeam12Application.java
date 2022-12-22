package com.team12.finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinalprojectParkEunbinTeam12Application {

    public static void main(String[] args) {
        SpringApplication.run(FinalprojectParkEunbinTeam12Application.class, args);
    }

}
