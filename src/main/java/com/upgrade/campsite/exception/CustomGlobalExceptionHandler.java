package com.upgrade.campsite.exception;

import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.PessimisticLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(null, headers, status);
    }

    @ExceptionHandler(value = {
            ConstraintViolationException.class,
            JsonParseException.class
    })
    public void constraintViolationException(HttpServletResponse response)
            throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = {PessimisticLockException.class})
    public ResponseEntity<Object> lockException(Exception ex) {
        return new  ResponseEntity<>(null, HttpStatus.LOCKED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> genericException(Exception ex) {
        return new  ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
