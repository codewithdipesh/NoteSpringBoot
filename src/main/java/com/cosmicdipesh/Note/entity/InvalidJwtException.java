package com.cosmicdipesh.Note.entity;

import jakarta.servlet.ServletException;

public class InvalidJwtException extends ServletException {
    public InvalidJwtException(String message) {
        super(message);
    }
}
