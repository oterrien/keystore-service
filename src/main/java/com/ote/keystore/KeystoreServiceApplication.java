package com.ote.keystore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class KeystoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeystoreServiceApplication.class, args);
    }
}
