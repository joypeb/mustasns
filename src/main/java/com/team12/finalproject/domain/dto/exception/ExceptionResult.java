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
    private String message;
}
