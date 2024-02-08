package com.rabobank.loans.infrastructure.http.advices;

import jakarta.annotation.Nullable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@EnableWebMvc
public class LoanCalculatorControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<DefaultErrorMessage> handleAllExceptions(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return ResponseEntity.badRequest().body(
                    DefaultErrorMessage.create(
                            "Invalid request",
                            List.of(ex.getMessage()
                            )
                    )
            );
        } else return ResponseEntity.internalServerError()
                .body(DefaultErrorMessage.create(
                        "Unexpected Error",
                        List.of()
                ));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders headers,
            @Nullable HttpStatusCode status,
            @Nullable WebRequest request
    ) {
        var errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        var response = DefaultErrorMessage.create(
                "Invalid request",
                errors
        );
        return ResponseEntity
                .badRequest()
                .body(response);
    }
}