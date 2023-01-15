package com.techblog.exception;

import com.techblog.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.NOT_FOUND), Instant.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        Map<String,String> response=new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError)error).getField();
            String message= error.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleEmailExistException(EmailAlreadyExistException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false ,String.valueOf(HttpStatus.CONFLICT),Instant.now()),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(FileFormatNotSupportException.class)
    public ResponseEntity<ApiResponse> handleFileFormatException(FileFormatNotSupportException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeException(MaxUploadSizeExceededException exception)
    {
        return new ResponseEntity<>(new ApiResponse("File size must be within 5MB", false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeException(MethodArgumentTypeMismatchException exception)
    {
        return new ResponseEntity<>(new ApiResponse("Id must be Integer type", false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMethodNotSupportException(HttpRequestMethodNotSupportedException exception)
    {
        return new ResponseEntity<>(new ApiResponse("This url not supported for this method",false,String.valueOf(HttpStatus.METHOD_NOT_ALLOWED),Instant.now()),HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleRequestParameter(MissingServletRequestParameterException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getParameterName()+" required in request parameter",false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(MultipartException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse> handleServletRequestPartException(MissingServletRequestPartException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullPointerException(NullPointerException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleNotReadableFormat(HttpMessageNotReadableException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleNotSupportedMediaType(HttpMediaTypeNotSupportedException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleFileNotFound(FileNotFoundException exception)
    {
        return new ResponseEntity<>(new ApiResponse("image not available",false,String.valueOf(HttpStatus.NOT_FOUND),Instant.now()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ApiResponse> handleImageNotFound(ImageNotFoundException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.NOT_FOUND),Instant.now()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AdminCodeNotMatchException.class)
    public ResponseEntity<ApiResponse> handleAdminCodeMismatchException(AdminCodeNotMatchException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),false,String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RoleFetchingException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(RoleFetchingException exception)
    {
        return new ResponseEntity<>(new ApiResponse("Something went wrong! Unable to register,Try later",false,String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),Instant.now()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException exception)
    {
        return new ResponseEntity<>(new ApiResponse("Something went wrong! Try later",false,String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),Instant.now()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
