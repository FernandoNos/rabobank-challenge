package com.rabobank.loans.infrastructure.http;

import com.rabobank.loans.domain.LoanServiceInterface;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimateRequestDTO;
import com.rabobank.loans.infrastructure.http.dtos.CalculateLoanEstimationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/loans")
public class LoanCalculatorController {
    private final LoanServiceInterface loanServiceInterface;

    public LoanCalculatorController(LoanServiceInterface loanServiceInterface) {
        this.loanServiceInterface = loanServiceInterface;
    }

    @PostMapping("/emi/estimates")
    public ResponseEntity<CalculateLoanEstimationResponseDTO> calculateEstimate(
            @Valid @RequestBody CalculateLoanEstimateRequestDTO request
    ) {
        var result = loanServiceInterface.calculate(request);
        var responseDTO = CalculateLoanEstimationResponseDTO.createResponse(result.doubleValue());
        return ResponseEntity.ok(responseDTO);
    }
}