package com.rabobank.loans.domain;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;
import com.rabobank.loans.infrastructure.repositories.UserRepository;
import com.rabobank.loans.infrastructure.repositories.entities.LoanEstimate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class LoanServiceTest {
    @Autowired
    private LoanServiceInterface loanServiceInterface;
    @Autowired
    private UserRepository userRepository;

    private static void assertCalculations(LoanEstimate calculation, BigDecimal result) {
        Assertions.assertEquals(result.doubleValue(), calculation.getValue());
        Assertions.assertEquals(CalculationType.EMI, calculation.getType());
        Assertions.assertNotNull(calculation.getCreatedAt());
        Assertions.assertEquals(calculation.getCreatedAt().toLocalDate(), LocalDate.now());
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void calculate_valid_request() {
        var request = createRequest("a@test.com");
        var result = loanServiceInterface.calculate(request);
        var optionalUser = userRepository.findByEmail(request.getEmail());
        Assertions.assertTrue(optionalUser.isPresent());
        var user = optionalUser.get();

        Assertions.assertNotNull(user.getCreatedAt());
        Assertions.assertEquals(request.getEmail(), user.getEmail());

        var calculations = user.getEstimates();
        Assertions.assertEquals(1, calculations.size());
        assertCalculations(calculations.get(0), result);
    }

    @Test
    public void calculate_multiple_requests_same_user() {
        var request = createRequest("a@test.com");
        var secondRequest = createRequest("a@test.com");

        var result = loanServiceInterface.calculate(request);
        var secondResult = loanServiceInterface.calculate(secondRequest);

        var optionalUser = userRepository.findByEmail(request.getEmail());
        Assertions.assertTrue(optionalUser.isPresent());
        var user = optionalUser.get();

        Assertions.assertNotNull(user.getCreatedAt());
        Assertions.assertEquals(request.getEmail(), user.getEmail());

        var calculations = user.getEstimates();
        Assertions.assertEquals(2, calculations.size());
        assertCalculations(calculations.get(0), result);
        assertCalculations(calculations.get(1), secondResult);
    }

    @Test
    public void calculate_multiple_requests_different_users() {
        var request = createRequest("a@test.com");
        var secondRequest = createRequest("b@test.com");

        var result = loanServiceInterface.calculate(request);
        var secondResult = loanServiceInterface.calculate(secondRequest);

        var optionalUser = userRepository.findByEmail(request.getEmail());
        var secondOptionalUser = userRepository.findByEmail(secondRequest.getEmail());
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertTrue(secondOptionalUser.isPresent());
        var user = optionalUser.get();
        var secondUser = optionalUser.get();

        Assertions.assertNotNull(user.getCreatedAt());
        Assertions.assertEquals(request.getEmail(), user.getEmail());

        var calculations = user.getEstimates();
        Assertions.assertEquals(1, calculations.size());
        assertCalculations(calculations.get(0), result);

        calculations = secondUser.getEstimates();
        Assertions.assertEquals(1, calculations.size());
        assertCalculations(calculations.get(0), secondResult);
    }

    private CalculateLoanEstimateRequestDTO createRequest(String email) {
        var req = new CalculateLoanEstimateRequestDTO(
                1.0,
                1.0,
                1.0,
                email
        );

        return req;
    }
}
