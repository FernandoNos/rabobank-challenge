package com.rabobank.loans.infrastructure.repositories;

import com.rabobank.loans.infrastructure.repositories.entities.LoanEstimate;
import org.springframework.data.repository.CrudRepository;

public interface CalculationRepository extends CrudRepository<LoanEstimate, Long> {
}
