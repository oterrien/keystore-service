package com.ote.keystore.trace.annotation;

import org.slf4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Traceable {

    Level level();

    enum Level {
        TRACE(Logger::isTraceEnabled, Logger::trace, Logger::trace),
        DEBUG(Logger::isDebugEnabled, Logger::debug, Logger::debug),
        INFO(Logger::isInfoEnabled, Logger::info, Logger::info),
        WARNING(Logger::isWarnEnabled, Logger::warn, Logger::warn),
        ERROR(Logger::isErrorEnabled, Logger::error, Logger::error);

        private Predicate<Logger> enabledPredicate;
        private BiConsumer<Logger, String> loggerConsumer;
        private TriConsumer<Logger, String, Throwable> loggerConsumerWithException;

        Level(Predicate<Logger> enabledPredicate,
              BiConsumer<Logger, String> loggerConsumer,
              TriConsumer<Logger, String, Throwable> loggerConsumerWithException) {

            this.enabledPredicate = enabledPredicate;
            this.loggerConsumer = loggerConsumer;
            this.loggerConsumerWithException = loggerConsumerWithException;
        }

        public boolean isEnabled(Logger logger) {
            return enabledPredicate.test(logger);
        }

        public void log(Logger logger, String param) {
            loggerConsumer.accept(logger, param);
        }

        public void log(Logger logger, String param, Throwable throwable) {
            loggerConsumerWithException.accept(logger, param, throwable);
        }
    }

    @FunctionalInterface
    interface TriConsumer<A, B, C> {

        void accept(A a, B b, C c);
    }
}