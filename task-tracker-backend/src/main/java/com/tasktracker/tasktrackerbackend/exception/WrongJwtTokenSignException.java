package com.tasktracker.tasktrackerbackend.exception;

public class WrongJwtTokenSignException extends RuntimeException {
    private static final String MESSAGE = "Wrong sign";

    public WrongJwtTokenSignException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
