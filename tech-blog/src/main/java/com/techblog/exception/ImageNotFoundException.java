package com.techblog.exception;

public class ImageNotFoundException extends RuntimeException{

    private String message;

    public ImageNotFoundException(String message)
    {
        super(message);
        this.message=message;
    }
}
