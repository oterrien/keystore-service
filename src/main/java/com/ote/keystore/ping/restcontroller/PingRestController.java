package com.ote.keystore.ping.restcontroller;

import com.ote.keystore.trace.annotation.Traceable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1/keys")
@Slf4j
public class PingRestController {

    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Traceable(level = Traceable.Level.TRACE)
    public void ping() {
        log.trace("ping #" + counter.getAndIncrement());
    }
}
