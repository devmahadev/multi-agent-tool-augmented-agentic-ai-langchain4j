package com.agentic.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class AiApplication {
    static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        SpringApplication.run(AiApplication.class, args);
    }
}
