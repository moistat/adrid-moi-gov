package com.example.pentaho.exception;


public class MoiException extends RuntimeException {

    public MoiException() {
        super();
    }

    public MoiException(String message) {
        super(message);
    }

    public MoiException(String message, Throwable cause) {
        super(message, cause);
    }
}

