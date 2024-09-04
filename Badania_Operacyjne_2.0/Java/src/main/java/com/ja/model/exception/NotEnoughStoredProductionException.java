package com.ja.model.exception;

public class NotEnoughStoredProductionException extends Exception {

    public NotEnoughStoredProductionException(String message) {
        super(message);
    }

    public NotEnoughStoredProductionException() {
        super();
    }
}
