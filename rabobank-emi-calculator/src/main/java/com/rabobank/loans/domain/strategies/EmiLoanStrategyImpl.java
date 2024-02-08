package com.rabobank.loans.domain.strategies;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.domain.valueObjects.CalculateLoanEstimate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EmiLoanStrategyImpl implements LoanStrategyInterface {

    private final static int MONTHS_IN_A_YEAR = 12;

    /**
     * @param calculateLoanEstimate receives object that contains all the necessary values to calculate the EMI
     *                              EMI = P x R x (1+R)^N / (1+R)^N-1
     *                              Being P = loan amount, R = monthly interest rate,and N is the tenure in months
     * @return EMI value
     */
    @Override
    public BigDecimal calculate(CalculateLoanEstimate calculateLoanEstimate) {
        if (calculateLoanEstimate == null) {
            throw new IllegalArgumentException("CalculateLoan cannot be null");
        }
        validateParams(calculateLoanEstimate);

        var monthlyInterestRate = calculateLoanEstimate.getYearlyInterestRate().divide(BigDecimal.valueOf(MONTHS_IN_A_YEAR), 10, RoundingMode.HALF_EVEN);
        var termInMonths = termInYearsToMonths(calculateLoanEstimate.getLoanTermInYears());

        // 1 + R
        BigDecimal onePlusMonthlyInterestRate = BigDecimal.ONE.add(monthlyInterestRate);
        // (1 + R)^N
        BigDecimal powFactor = onePlusMonthlyInterestRate.pow(termInMonths);

        // P x R x ( 1 + R )^N
        BigDecimal numerator = calculateLoanEstimate.getLoanValue().multiply(monthlyInterestRate).multiply(powFactor);
        // ( 1 + R )^N - 1
        BigDecimal denominator = powFactor.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_EVEN);
    }

    @Override
    public Boolean isApplicable(CalculationType type) {
        return CalculationType.EMI.equals(type);
    }

    private void validateParams(CalculateLoanEstimate calculateLoanEstimate) {
        if (
                calculateLoanEstimate.getLoanValue().doubleValue() <= 0.0 ||
                        calculateLoanEstimate.getLoanTermInYears().doubleValue() <= 0.0 ||
                        calculateLoanEstimate.getYearlyInterestRate().doubleValue() <= 0.0
        ) {
            throw new IllegalArgumentException("loan value, terms in years, and interest rate values must be greater than 0");
        }

        if (calculateLoanEstimate.getYearlyInterestRate().doubleValue() > 100) {
            throw new IllegalArgumentException("yearly interest rate must not be greater than 100");
        }

        if (calculateLoanEstimate.getLoanTermInYears().doubleValue() > 30) {
            throw new IllegalArgumentException("Loan Term in Years must not be greater than 30");
        }
    }

    private Integer termInYearsToMonths(BigDecimal termInYeras) {
        // given a term in 5.5 years, full years is 5, and partialYear is 0.5
        var fullYears = termInYeras.intValue();
        var partialYear = termInYeras.subtract(BigDecimal.valueOf(fullYears));
        return fullYears * MONTHS_IN_A_YEAR + partialYear.multiply(BigDecimal.valueOf(MONTHS_IN_A_YEAR)).intValue();
    }

}
