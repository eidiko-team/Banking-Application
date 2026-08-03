package com.example.cruds.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

//Request → DispatcherServlet → Controller → Service → Repository → Exception thrown → DispatcherServlet
//intercepts it → @RestControllerAdvice finds the matching @ExceptionHandler → Builds

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponse response = new ErrorResponse();

        response.setStatus(400);
        response.setMessage("Validation failed");
        response.setErrors(errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> customerNotFound(CustomerNotFoundException customerNotFoundException){
        return ResponseEntity.badRequest().body(customerNotFoundException.getMessage());
    }
}
