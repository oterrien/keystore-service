package com.ote.keystore.exceptionhandler;

import com.ote.keystore.credential.persistence.CredentialPersistenceService;
import com.ote.keystore.cryptor.service.CryptorService;
import com.ote.keystore.trace.annotation.Traceable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public String handle(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public String handle(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public BeanInvalidationResult handle(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return new BeanInvalidationResult(e.getBindingResult());
    }

    @ExceptionHandler(CredentialPersistenceService.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @Traceable(level = Traceable.Level.INFO)
    public String handle(CredentialPersistenceService.NotFoundException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(CredentialPersistenceService.NotMergeableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public String handle(CredentialPersistenceService.NotMergeableException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(CryptorService.EncryptException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public String handle(CryptorService.EncryptException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(CryptorService.DecryptException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Traceable(level = Traceable.Level.ERROR)
    public String handle(CryptorService.DecryptException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }
}
