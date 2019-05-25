package com.space.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое, если параметры запроса не валидные
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        this("Bad request parameters");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
