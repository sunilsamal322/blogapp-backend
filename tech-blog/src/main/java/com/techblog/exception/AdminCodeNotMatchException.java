package com.techblog.exception;

public class AdminCodeNotMatchException extends RuntimeException{

    private String message;

    public AdminCodeNotMatchException(String message)
    {
        super(message);
        this.message=message;
    }
}
