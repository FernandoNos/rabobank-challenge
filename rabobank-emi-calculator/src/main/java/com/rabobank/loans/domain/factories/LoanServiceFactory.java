package com.rabobank.loans.domain.factories;

import com.rabobank.loans.domain.enums.CalculationType;
import com.rabobank.loans.domain.strategies.LoanStrategyInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanServiceFactory {
    private final List<LoanStrategyInterface> loanServices;

    public LoanServiceFactory(List<LoanStrategyInterface> loanServices) {
        this.loanServices = loanServices;
    }

    public LoanStrategyInterface getService(CalculationType calculationType) {
        var service = loanServices.stream().filter(s -> s.isApplicable(calculationType))
                .toList();
        if (service.isEmpty()) {
            throw new IllegalArgumentException("Invalid calculation type requested!");
        }
        return service.get(0);
    }
}
