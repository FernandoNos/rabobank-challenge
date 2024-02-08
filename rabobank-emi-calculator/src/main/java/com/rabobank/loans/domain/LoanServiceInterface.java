package com.rabobank.loans.domain;

import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;

import java.math.BigDecimal;

public interface LoanServiceInterface {
    BigDecimal calculate(CalculateLoanEstimateRequestDTO requestDTO);
}
