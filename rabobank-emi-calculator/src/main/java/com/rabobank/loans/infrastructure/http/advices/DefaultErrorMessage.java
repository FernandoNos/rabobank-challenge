package com.rabobank.loans.infrastructure.http.advices;

import java.util.List;

public class DefaultErrorMessage {
    private String message;
    private List<String> errors;

    public static DefaultErrorMessage create(String message, List<String> errors) {
        var errorMessage = new DefaultErrorMessage();
        errorMessage.setErrors(errors);
        errorMessage.setMessage(message);
        return errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
