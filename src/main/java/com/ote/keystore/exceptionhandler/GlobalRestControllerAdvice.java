package com.ote.keystore.exceptionhandler;

import com.ote.keystore.credential.persistence.CredentialPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(CredentialPersistenceService.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(CredentialPersistenceService.NotFoundException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(CredentialPersistenceService.NotMergeableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(CredentialPersistenceService.NotMergeableException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }
}
