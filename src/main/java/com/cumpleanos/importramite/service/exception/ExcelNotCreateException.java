package com.cumpleanos.importramite.service.exception;

public class ExcelNotCreateException extends RuntimeException {
    public ExcelNotCreateException(String message) {
        super(message);
    }
    public ExcelNotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
