package com.ote.keystore.cryptor.restcontroller;

import com.ote.keystore.cryptor.service.SecretKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * This RestController aims to format secretKey with a common way
  */
@RestController
@RequestMapping("/v1/secretKeys")
@Slf4j
public class SecretKeyRestController {

    @Autowired
    private SecretKeyService secretKeyService;

    /**
     * When front-end would like to encrypt something, this endpoint aims to format the given secretKey
     * @param secretKey
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{secretKey}")
    @ResponseStatus(HttpStatus.OK)
    public String transformSecretKey(@PathVariable String secretKey) {
        return secretKeyService.getSecretKey(secretKey);
    }
}
