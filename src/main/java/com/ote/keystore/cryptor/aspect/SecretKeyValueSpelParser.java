package com.ote.keystore.cryptor.aspect;

import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Function;

public class SecretKeyValueSpelParser {

    @Getter
    private final Function<String, String> value;

    public SecretKeyValueSpelParser(ProceedingJoinPoint point) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        ExpressionParser parser = new SpelExpressionParser();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(point.getTarget(), method, point.getArgs(), new ProceedingJoinPointParameterNameDiscoverer(point));

        this.value = (expression -> parser.parseExpression(expression).getValue(context, String.class));
    }

    private static class ProceedingJoinPointParameterNameDiscoverer implements ParameterNameDiscoverer {

        private final MethodSignature signature;

        private ProceedingJoinPointParameterNameDiscoverer(ProceedingJoinPoint point) {
            this.signature = (MethodSignature) point.getSignature();
        }

        @Override
        public String[] getParameterNames(Method method) {
            return this.signature.getParameterNames();
        }

        @Override
        public String[] getParameterNames(Constructor<?> constructor) {
            return this.signature.getParameterNames();
        }
    }
}