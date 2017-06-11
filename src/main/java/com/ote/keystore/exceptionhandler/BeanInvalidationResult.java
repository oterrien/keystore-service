package com.ote.keystore.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeanInvalidationResult {

    private Object target;

    private final List<Invalidation> invalidations = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Invalidation {

        private String field;
        private String type;
        private String message;

        public Invalidation(FieldError fieldError) {
            this.setField(fieldError.getField());
            this.setType(fieldError.getCodes()[3]);
            this.setMessage(fieldError.getField() + " " + fieldError.getDefaultMessage());
        }
    }

    public BeanInvalidationResult(BindingResult bindingResult) {

        this.target = bindingResult.getTarget();
        bindingResult.getFieldErrors().stream().map(Invalidation::new).forEach(invalidations::add);
    }
}
