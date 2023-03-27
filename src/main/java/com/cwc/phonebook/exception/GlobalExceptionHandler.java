package com.cwc.phonebook.exception;

import com.cwc.phonebook.utils.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(new Date()
                ,ex.getMessage()
                ,request.getDescription(false))
                , HttpStatus.BAD_REQUEST);
    }

}
