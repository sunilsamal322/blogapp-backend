package com.techblog.exception;

public class FileFormatNotSupportException extends RuntimeException{

    private String fileExtension;

    public FileFormatNotSupportException(String fileExtension)
    {
        super(String.format("Supported File Format JPG/JPEG/PNG, %s file is not supported",fileExtension));
        this.fileExtension=fileExtension;
    }
}
