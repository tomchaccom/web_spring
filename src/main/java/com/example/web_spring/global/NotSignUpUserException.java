package com.example.web_spring.global;

public class NotSignUpUserException extends RuntimeException {
    public NotSignUpUserException(String message) {
        super(message);
    }
}
