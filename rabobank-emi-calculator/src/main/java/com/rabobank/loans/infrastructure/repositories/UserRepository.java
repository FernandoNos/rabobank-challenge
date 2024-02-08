package com.rabobank.loans.infrastructure.repositories;

import com.rabobank.loans.infrastructure.repositories.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
