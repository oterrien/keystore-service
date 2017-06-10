package com.ote.keystore.ping.restcontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1/keys")
@Slf4j
public class PingRestController {

    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean ping() {
        log.trace("ping #" + counter.getAndIncrement());
        return true;
    }
}
