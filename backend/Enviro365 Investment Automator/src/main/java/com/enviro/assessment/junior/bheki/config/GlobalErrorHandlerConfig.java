package com.enviro.assessment.junior.bheki.config;

import com.enviro.assessment.junior.bheki.dto.ApiErrorResponse;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandlerConfig {


    @ExceptionHandler(value = ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleException(ApiException e) {
        return  apiExceptionResponse400(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleException(Exception e) {
        log.info("Uncaught Exception", e);
        return  apiExceptionResponse400("Server Error!!!!");
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationExceptions(Exception e) {
        return  apiExceptionResponse400("Invalid Request!!!!");
    }

    private ApiErrorResponse apiExceptionResponse400(String message) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage(message);
        apiErrorResponse.setStatus(400);
        apiErrorResponse.setTimestamp(LocalDateTime.now());
        return apiErrorResponse;
    }







}
