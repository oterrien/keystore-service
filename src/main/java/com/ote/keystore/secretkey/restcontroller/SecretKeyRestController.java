package com.ote.keystore.secretkey.restcontroller;

import com.ote.keystore.trace.annotation.Traceable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/keys/")
@Slf4j
public class SecretKeyRestController {

    // password par défaut (changeit) --> encodé en SHA256

    // Connect : encoder automatiquement le password reçu en SHA256 et comparer avec la valeur SHA256 de la base
    // Si la valeur reçu est correct alors la secretKey reçu peut être utilisée pour encoder/décoder

    // create password --> encode password en

    @RequestMapping(value = "/{secretKey}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public void updateSecretKey(@PathVariable String secretKey, @RequestParam String test) {
        log.trace("update secretKey");
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public String test(@RequestParam String test,@RequestParam String test01) {
        log.trace("update secretKey");
        return "";
    }
}
