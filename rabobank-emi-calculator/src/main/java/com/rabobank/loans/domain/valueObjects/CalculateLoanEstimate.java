package com.rabobank.loans.domain.valueObjects;

import com.rabobank.loans.domain.enums.CalculationType;

import java.math.BigDecimal;

public class CalculateLoanEstimate {
    private BigDecimal loanValue;
    private BigDecimal yearlyInterestRate;
    private BigDecimal loanTermInYears;
    private CalculationType type;

    public static CalculateLoanEstimate create(
            BigDecimal loanValue,
            BigDecimal yearlyInterestRate,
            BigDecimal loanTermInYears,
            CalculationType type
    ) {
        var calculateLoan = new CalculateLoanEstimate();
        calculateLoan.setLoanValue(loanValue);
        calculateLoan.setLoanTermInYears(loanTermInYears);
        calculateLoan.setType(type);
        calculateLoan.setYearlyInterestRate(yearlyInterestRate);
        return calculateLoan;
    }

    public BigDecimal getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue) {
        this.loanValue = loanValue;
    }

    public BigDecimal getYearlyInterestRate() {
        return yearlyInterestRate;
    }

    public void setYearlyInterestRate(BigDecimal yearlyInterestRate) {
        this.yearlyInterestRate = yearlyInterestRate;
    }

    public BigDecimal getLoanTermInYears() {
        return loanTermInYears;
    }

    public void setLoanTermInYears(BigDecimal loanTermInYears) {
        this.loanTermInYears = loanTermInYears;
    }

    public CalculationType getType() {
        return type;
    }

    public void setType(CalculationType type) {
        this.type = type;
    }
}
