package com.example.cruds.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException{

    public CustomerAlreadyExistsException(String msg) {
        super(msg);
    }
}
