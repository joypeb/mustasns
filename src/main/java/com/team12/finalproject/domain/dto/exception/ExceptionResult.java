package com.team12.finalproject.domain.dto.exception;

import com.team12.finalproject.exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResult {
    private ErrorCode errorCode;
    private int errorCodeInt;
    private String message;

    public ExceptionResult(int errorCodeInt, String message) {
        this.errorCodeInt = errorCodeInt;
        this.message = message;
    }

    public ExceptionResult(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
