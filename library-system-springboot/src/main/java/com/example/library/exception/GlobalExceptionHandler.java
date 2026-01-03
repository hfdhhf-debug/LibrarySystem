package com.example.library.exception;

import com.example.library.dto.ApiResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResp<Void>> handleBadRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(ApiResp.fail(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResp<Void>> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404).body(ApiResp.fail(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResp<Void>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(ApiResp.fail(msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResp<Void>> handleOther(Exception e) {
        // In production you should not expose e.getMessage(); keep it simple for coursework.
        return ResponseEntity.internalServerError().body(ApiResp.fail("Server error: " + e.getMessage()));
    }
}
