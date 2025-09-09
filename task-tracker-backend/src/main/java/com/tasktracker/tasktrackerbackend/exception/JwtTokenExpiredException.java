package com.tasktracker.tasktrackerbackend.exception;

public class JwtTokenExpiredException extends RuntimeException {
    private static final String MESSAGE = "Token's lifetime expired";

    public JwtTokenExpiredException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
