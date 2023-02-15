package com.example.palianytsia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PalianytsiaApplication {

    public static void main(String[] args) {
        log.info("Application runs");
        SpringApplication.run(PalianytsiaApplication.class, args);
    }

}
