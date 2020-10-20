package com.rccl.demo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultipleAuthApplication {

    /**
     *
     * Whenever we want to have a new auth provider,
     * we just add a new AuthenticationService (like {@link com.rccl.demo.web.service.impl.FidelioAuthenticationService})
     * and a new endpoint at {@link com.rccl.demo.web.security.AuthenticationController}
     */
    public static void main(String[] args) {
        SpringApplication.run(MultipleAuthApplication.class, args);
    }
}
