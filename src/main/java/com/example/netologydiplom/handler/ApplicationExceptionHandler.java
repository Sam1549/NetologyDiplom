package com.example.netologydiplom.handler;

import com.example.netologydiplom.dto.response.ExceptionResponse;
import com.example.netologydiplom.exceptions.FileCloudException;
import com.example.netologydiplom.exceptions.InputDataException;
import com.example.netologydiplom.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handlerUnauthorizedException(UnauthorizedException ex) {
        log.warn(ex.getMessage());
        //return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), 401), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(401).body(new ExceptionResponse(ex.getMessage(), 401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InputDataException.class)
    public ResponseEntity<ExceptionResponse> handlerInputDataException(InputDataException ex) {
        log.warn(ex.getMessage());
        // return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), 400), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex.getMessage(), 400));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileCloudException.class)
    public ResponseEntity<ExceptionResponse> handlerFileCloudException(FileCloudException ex) {
        log.warn(ex.getMessage());
        // return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), 500), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.internalServerError().body(new ExceptionResponse(ex.getMessage(), 500));
    }

}
