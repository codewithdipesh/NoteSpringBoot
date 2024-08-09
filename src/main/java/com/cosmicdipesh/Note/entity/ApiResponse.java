package com.cosmicdipesh.Note.entity;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String message = "";
    private T data ;
    private int status;

    // Constructor
    public ApiResponse(String message, T data, int status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

}
