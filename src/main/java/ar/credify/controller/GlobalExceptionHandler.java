package ar.credify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleFileSizeExceededException(MultipartException ex, HttpServletRequest request) {
        request.setAttribute("statusCode", HttpStatus.PAYLOAD_TOO_LARGE.value());
        request.setAttribute("message", "The file you're trying to upload is too large.");
        request.setAttribute("subMessage", "Please upload a smaller file.");
        return "error";
    }

}