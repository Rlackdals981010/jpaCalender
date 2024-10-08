package com.kcm.jpacalender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing //1단계. 시간사용
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class JpaCalenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaCalenderApplication.class, args);
    }

}
