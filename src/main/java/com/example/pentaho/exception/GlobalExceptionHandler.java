package com.example.pentaho.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 捕獲所有異常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Exception e, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return "error";
    }

    // 捕獲具體的異常，例如 NullPointerException
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNullPointerException(NullPointerException e, Model model) {
        model.addAttribute("errorMessage", "Null Pointer Exception: " + e.getMessage());
        return "error";
    }

    // 捕獲自定義的異常
    @ExceptionHandler(MoiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCustomException(MoiException e, Model model) {
        model.addAttribute("errorMessage", "Custom Error: " + e.getMessage());
        return "error";
    }
}