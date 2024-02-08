package com.rabobank.loans.infrastructure.http.dtos;

public class CalculateLoanEstimationResponseDTO {
    private Double value;

    public static CalculateLoanEstimationResponseDTO createResponse(Double value) {
        var response = new CalculateLoanEstimationResponseDTO();
        response.setValue(value);
        return response;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
