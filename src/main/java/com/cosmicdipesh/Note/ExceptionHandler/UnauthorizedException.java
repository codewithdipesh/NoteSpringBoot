package com.cosmicdipesh.Note.ExceptionHandler;



public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
