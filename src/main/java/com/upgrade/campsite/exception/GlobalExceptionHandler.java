package com.upgrade.campsite.exception;

import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.PessimisticLockException;

import com.upgrade.campsite.Constant;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {
            ConstraintViolationException.class,
            JsonParseException.class
    })
    public void handleConstraintViolationException(HttpServletResponse response)
            throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = {ApiErrorException.class})
    public ResponseEntity<Object> handleApiErrorException(ApiErrorException ex) {
        return new ResponseEntity<>(new ApiError(ex.getApiErrorCode(), ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PessimisticLockException.class})
    public ResponseEntity<Object> handleLockException(PessimisticLockException pLockEx) {
        LOGGER.error("Locked resource (PessimisticLockException).", pLockEx);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.LOCKED.value(), Constant.RESOURCE_LOCKED_MSG),
                new HttpHeaders(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constant.UNKNOWN_ERROR_MSG),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
