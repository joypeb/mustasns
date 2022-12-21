package com.team12.finalproject.exception;

import com.team12.finalproject.domain.dto.exception.Exception;
import com.team12.finalproject.domain.dto.exception.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appExceptionHandler(AppException e) {
        Exception<ExceptionResult> exception = new Exception<>("ERROR",new ExceptionResult(e.getErrorCode(),e.getMessage()));
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(exception);
    }
}
