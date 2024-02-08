package com.rabobank.loans.domain.strategies;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.domain.valueObjects.CalculateLoanEstimate;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class EmiLoanStrategyTest {
    private final EmiLoanStrategyImpl loanFacadeInterface = new EmiLoanStrategyImpl();

    public static Stream<Arguments> provideCalculationScenarios() {
        var email = "a@test.com";

        // email, loanValue, yearly interest rate, years, expected value
        return Stream.of(
                Arguments.of(email, 258000.0, 0.0135, 30.0, 871.96),
                Arguments.of(email, 25000.0, 0.044, 1.5, 1437.77),
                Arguments.of(email, 1.0, 1.0, 1.0, 0.13),
                Arguments.of(email, 250000.0, 0.044, 30.0, 1251.9),
                Arguments.of(email, 100000.0, 0.05, 5.0, 1887.12),
                Arguments.of(email, 100000.0, 0.5, 5.0, 4560.47),
                Arguments.of(email, 10000.0, 0.06, 5.5, 178.26),
                Arguments.of(email, Double.MAX_VALUE, 0.05, 5.0, 3.392468720116531E306)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculationScenarios")
    public void testCalculationScenarios(
            String email,
            Double loanValue,
            Double yearlyInterestRate,
            Double years,
            Double expectedResult
    ) {
        var req = new CalculateLoanEstimate();
        req.setType(CalculationType.EMI);
        req.setLoanValue(BigDecimal.valueOf(loanValue));
        req.setYearlyInterestRate(BigDecimal.valueOf(yearlyInterestRate));
        req.setLoanTermInYears(BigDecimal.valueOf(years));
        var result = loanFacadeInterface.calculate(req);
        Assertions.assertEquals(expectedResult, result.doubleValue());
    }

    private CalculateLoanEstimateRequestDTO createRequest(String email) {
        return new CalculateLoanEstimateRequestDTO(
                1.0,
                1.0,
                1.0,
                email
        );
    }
}
