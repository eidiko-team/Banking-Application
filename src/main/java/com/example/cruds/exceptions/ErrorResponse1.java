package com.example.cruds.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse1 {

    private int statusCode;
    private String message;

    public ErrorResponse1(String message)
    {
        super();
        this.message = message;
    }
}