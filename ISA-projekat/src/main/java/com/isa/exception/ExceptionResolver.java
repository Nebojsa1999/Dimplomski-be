package com.isa.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionResolver {

    public static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> illegalArgument(IllegalArgumentException exception, WebRequest request) {
        LOG.error("Illegal argument", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(HttpServletRequest request, NotFoundException exception) {
        LOG.error("Nor found exception", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception exception, WebRequest request) {
        LOG.error("Error", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
