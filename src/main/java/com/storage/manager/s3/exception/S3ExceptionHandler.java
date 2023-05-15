package com.storage.manager.s3.exception;

import com.amazonaws.Response;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class S3ExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AmazonS3Exception.class)
    public final ResponseEntity<Object> amazon(AmazonS3Exception ex, WebRequest request) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("error", ex.getMessage());
        map.put("details", ex.getAdditionalDetails());
        map.put("message", ex.getErrorMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
