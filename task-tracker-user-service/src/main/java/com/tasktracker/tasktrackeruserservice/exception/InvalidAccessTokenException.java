package com.tasktracker.tasktrackeruserservice.exception;

public class InvalidAccessTokenException extends RuntimeException {
    private static final String MESSAGE = "Invalid access token";

    public InvalidAccessTokenException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
