package com.kristjan.movie.store.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(RuntimeException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTimestamp(new Date());
        errorMessage.setStatus(400);
        errorMessage.setError(e.getMessage());
        return ResponseEntity.badRequest().body(errorMessage);
    }

}
