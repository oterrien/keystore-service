package com.ote.keystore.cryptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class Key128BitsConfiguration extends Key128BitsAspect {

    @PostConstruct
    public void init() {
        log.warn("####- KeyConfiguration is enabled");
    }
}