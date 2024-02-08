package com.rabobank.loans.infrastructure.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.loans.domain.LoanServiceInterface;
import com.rabobank.loans.infrastructure.http.advices.DefaultErrorMessage;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimationResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoanCalculatorControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoanServiceInterface loanServiceInterface;

    private static Stream<Arguments> getBadRequests() {
        return Stream.of(
                Arguments.of(createRequestWithDefaultValues(-1.0, null, null, null), "Loan value MUST be positive"),
                Arguments.of(createRequestWithDefaultValues(null, -1.0, null, null), "Yearly interest rate MIN value is 0"),
                Arguments.of(createRequestWithDefaultValues(null, 101.0, null, null), "Yearly interest rate MAX value is 100"),
                Arguments.of(createRequestWithDefaultValues(null, null, -1.0, null), "Loan Term in Year MIN value is 0"),
                Arguments.of(createRequestWithDefaultValues(null, null, 31.0, null), "Loan Term in Year MAX value is 30"),
                Arguments.of(createRequestWithDefaultValues(null, null, null, "a"), "Invalid email received!")
        );
    }

    public static Stream<Arguments> getBadRequestsWitInvalidValues() {
        var req1 = new CalculateLoanEstimateRequestDTO(
                null,
                0.06,
                5.0,
                "test@test.com");
        var req2 = new CalculateLoanEstimateRequestDTO(
                1.0,
                null,
                5.0,
                "test@test.com");
        var req3 = new CalculateLoanEstimateRequestDTO(
                1.0,
                0.06,
                null,
                "test@test.com");
        var req4 = new CalculateLoanEstimateRequestDTO(
                1.0,
                0.06,
                5.0,
                null);
        var req5 = new CalculateLoanEstimateRequestDTO(
                0.0,
                0.06,
                5.0,
                "test@test.com");
        var req6 = new CalculateLoanEstimateRequestDTO(
                1.0,
                0.0,
                5.0,
                "test@test.com");
        var req7 = new CalculateLoanEstimateRequestDTO(
                1.0,
                0.06,
                0.0,
                "test@test.com");

        return Stream.of(
                Arguments.of(req1, "Loan value MUST NOT be null"),
                Arguments.of(req2, "Yearly interest rate MUST NOT be null"),
                Arguments.of(req3, "Loan Term In Years MUST NOT be null"),
                Arguments.of(req4, "Email MUST NOT be null"),
                Arguments.of(req5, "loan value, terms in years, and interest rate values must be greater than 0"),
                Arguments.of(req6, "loan value, terms in years, and interest rate values must be greater than 0"),
                Arguments.of(req7, "loan value, terms in years, and interest rate values must be greater than 0")
        );
    }

    private static CalculateLoanEstimateRequestDTO createRequestWithDefaultValues(
            Double loanValue,
            Double yearlyInterestRate,
            Double loanTermInYears,
            String email
    ) {

        return new CalculateLoanEstimateRequestDTO(
                loanValue == null ? 10000.0 : loanValue,
                yearlyInterestRate == null ? 0.06 : yearlyInterestRate,
                loanTermInYears == null ? 5.0 : loanTermInYears,
                email == null ? "test@test.com" : email);
    }

    @Test
    void calculateEmi_WhenValidRequest_ThenReturnOk() throws Exception {
        CalculateLoanEstimateRequestDTO request = new CalculateLoanEstimateRequestDTO(
                10000.0,
                6.0,
                5.0,
                "test@test.com"
        );

        var result = callCalculate(request)
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        CalculateLoanEstimationResponseDTO responseDTO = objectMapper.readValue(responseBody, CalculateLoanEstimationResponseDTO.class);

        Assertions.assertEquals(193.33, responseDTO.getValue());
    }

    @ParameterizedTest
    @MethodSource("getBadRequests")
    void calculateEmi_WhenInvalidRequest_ThenReturnBadRequestWithDescription(
            CalculateLoanEstimateRequestDTO request,
            String message
    ) throws Exception {
        var result = callCalculate(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var responseDTO = objectMapper.readValue(responseBody, DefaultErrorMessage.class);
        Assertions.assertEquals(1, responseDTO.getErrors().size());
        Assertions.assertEquals(message, responseDTO.getErrors().get(0));

    }

    @ParameterizedTest
    @MethodSource("getBadRequestsWitInvalidValues")
    void calculateEmi_WhenInvalidRequestWithInvalidValues_ThenReturnBadRequestWithDescription(
            CalculateLoanEstimateRequestDTO request,
            String message
    ) throws Exception {
        var result = callCalculate(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var responseDTO = objectMapper.readValue(responseBody, DefaultErrorMessage.class);
        Assertions.assertEquals(1, responseDTO.getErrors().size());
        Assertions.assertEquals(message, responseDTO.getErrors().get(0));
    }

    private ResultActions callCalculate(CalculateLoanEstimateRequestDTO request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/loans/emi/estimates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
}
