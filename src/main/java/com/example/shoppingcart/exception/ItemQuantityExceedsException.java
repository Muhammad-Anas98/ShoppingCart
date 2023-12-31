package com.example.shoppingcart.exception;

public class ItemQuantityExceedsException extends RuntimeException {

    public ItemQuantityExceedsException(String message) {
        super(message);
    }
}
