package com.team12.finalproject.exception;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.exception.Exception;
import com.team12.finalproject.domain.dto.exception.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appExceptionHandler(AppException e) {
        ExceptionResult exceptionResult = new ExceptionResult(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(exceptionResult));
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e){
        ExceptionResult exceptionResult = new ExceptionResult(e.getErrorCode(),"DB에러입니다");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(exceptionResult));
    }
}
