package com.example.one_vote_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OneVoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneVoteServiceApplication.class, args);
    }

}
