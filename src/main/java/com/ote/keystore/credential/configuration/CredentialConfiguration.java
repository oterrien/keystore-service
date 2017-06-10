package com.ote.keystore.credential.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("${credential.config.location:classpath:credential.yml}")
@Configuration
@Data
@Slf4j
public class CredentialConfiguration {

    @Value("${page.default.size}")
    private String defaultPageSize;
}
