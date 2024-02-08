package com.rabobank.loans.infrastructure.http.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalculateLoanEstimateRequestDTO {
    @NotNull(message = "Loan value MUST NOT be null")
    @Min(value = 0, message = "Loan value MUST be positive")
    private Double loanValue;

    @NotNull(message = "Yearly interest rate MUST NOT be null")
    @Min(value = 0, message = "Yearly interest rate MIN value is 0")
    @Max(value = 100, message = "Yearly interest rate MAX value is 100")
    private Double yearlyInterestRate;

    @NotNull(message = "Loan Term In Years MUST NOT be null")
    @Min(value = 0, message = "Loan Term in Year MIN value is 0")
    @Max(value = 30, message = "Loan Term in Year MAX value is 30")
    private Double loanTermInYears;

    @NotNull(message = "Email MUST NOT be null")
    @Email(message = "Invalid email received!")
    private String email;

    public CalculateLoanEstimateRequestDTO() {

    }

    public CalculateLoanEstimateRequestDTO(Double loanValue, Double yearlyInterestRate, Double loanTermInYears, String email) {
        this.loanValue = loanValue;
        this.yearlyInterestRate = yearlyInterestRate;
        this.loanTermInYears = loanTermInYears;
        this.email = email;
    }

    public Double getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(Double loanValue) {
        this.loanValue = loanValue;
    }

    public Double getYearlyInterestRate() {
        return yearlyInterestRate;
    }

    public void setYearlyInterestRate(Double yearlyInterestRate) {
        this.yearlyInterestRate = yearlyInterestRate;
    }

    public Double getLoanTermInYears() {
        return loanTermInYears;
    }

    public void setLoanTermInYears(Double loanTermInYears) {
        this.loanTermInYears = loanTermInYears;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
