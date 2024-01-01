package com.example.shoppingcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemQuantityException extends RuntimeException {

    public ItemQuantityException(String message) {
        super(message);
    }
}
