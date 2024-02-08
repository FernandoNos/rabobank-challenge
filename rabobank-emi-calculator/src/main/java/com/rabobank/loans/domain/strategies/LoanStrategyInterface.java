package com.rabobank.loans.domain.strategies;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.domain.valueObjects.CalculateLoanEstimate;

import java.math.BigDecimal;

public interface LoanStrategyInterface {
    BigDecimal calculate(
            CalculateLoanEstimate calculateLoanEstimate
    );

    Boolean isApplicable(CalculationType type);
}
