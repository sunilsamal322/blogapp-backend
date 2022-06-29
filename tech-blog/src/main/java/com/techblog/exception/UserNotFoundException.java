package com.techblog.exception;

public class UserNotFoundException extends RuntimeException{

    private String email;

    public UserNotFoundException(String email)
    {
        super(String.format("Invalid email address"));
        this.email=email;
    }
}
