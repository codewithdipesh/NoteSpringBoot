package com.cosmicdipesh.Note.ExceptionHandler;



public class ExistingUserException extends RuntimeException {
    public ExistingUserException(String message) {
        super(message);
    }
}
