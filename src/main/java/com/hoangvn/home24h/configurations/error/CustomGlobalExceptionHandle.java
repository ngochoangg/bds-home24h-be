package com.hoangvn.home24h.configurations.error;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandle extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, Object> bodyResponse = new LinkedHashMap<>();
        bodyResponse.put("timestamp", new Date());
        bodyResponse.put("status", status.value());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
        bodyResponse.put("errors", errors);
        return new ResponseEntity<>(bodyResponse, headers, status);
    }
}
