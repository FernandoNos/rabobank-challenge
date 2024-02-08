package com.rabobank.loans.domain;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.domain.factories.LoanServiceFactory;
import com.rabobank.loans.domain.valueObjects.CalculateLoanEstimate;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;
import com.rabobank.loans.infrastructure.repositories.CalculationRepository;
import com.rabobank.loans.infrastructure.repositories.entities.LoanEstimate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class LoanServiceImpl implements LoanServiceInterface {
    private final LoanServiceFactory factory;
    private final UserServiceInterface userServiceInterface;
    private final CalculationRepository calculationRepository;

    public LoanServiceImpl(
            LoanServiceFactory factory,
            UserServiceInterface userServiceInterface,
            CalculationRepository calculationRepository
    ) {
        this.factory = factory;
        this.userServiceInterface = userServiceInterface;
        this.calculationRepository = calculationRepository;
    }

    @Override
    public BigDecimal calculate(CalculateLoanEstimateRequestDTO requestDTO) {
        var service = factory.getService(CalculationType.EMI);
        var calculateLoan = toCalculateLoan(requestDTO);

        var result = service.calculate(calculateLoan);
        saveCalculation(result, requestDTO.getEmail(), CalculationType.EMI);
        return result;
    }

    private CalculateLoanEstimate toCalculateLoan(CalculateLoanEstimateRequestDTO requestDTO) {
        return CalculateLoanEstimate.create(
                BigDecimal.valueOf(requestDTO.getLoanValue()),
                BigDecimal.valueOf(requestDTO.getYearlyInterestRate()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(requestDTO.getLoanTermInYears()),
                CalculationType.EMI
        );
    }

    private void saveCalculation(BigDecimal calcValue, String email, CalculationType type) {
        var user = userServiceInterface.getAndCreateUser(email);
        var newCalculation = LoanEstimate.create(
                calcValue.doubleValue(),
                user,
                type
        );
        calculationRepository.save(newCalculation);
    }
}
