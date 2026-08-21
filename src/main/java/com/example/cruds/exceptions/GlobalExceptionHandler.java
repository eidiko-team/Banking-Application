package com.example.cruds.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
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


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {

        System.out.println("Message: " + e.getMessage());

        System.out.println("Cause: " + e.getCause());


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException e) {

        System.out.println("==============================");

        //just prints the thrown message
        System.out.println("MESSAGE:");
        System.out.println(e.getMessage());

//        message+exception
        System.out.println("\nTO STRING:");
        System.out.println(e.toString());

        //if chaining the shows the case for the current exception
        System.out.println("\nCAUSE:");
        System.out.println(e.getCause());

        System.out.println("\nCAUSE MESSAGE:");

        if (e.getCause() != null) {
            System.out.println(e.getCause().getMessage());
        }


        System.out.println("\nSTACK TRACE:");
        e.printStackTrace();



//        getting and printing the stacktrace
        System.out.println(Arrays.toString(e.getStackTrace()));

        System.out.println("==============================");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }


}
