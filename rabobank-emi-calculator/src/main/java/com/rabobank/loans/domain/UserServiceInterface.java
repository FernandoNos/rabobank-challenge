package com.rabobank.loans.domain;

import com.rabobank.loans.infrastructure.repositories.entities.User;

public interface UserServiceInterface {
    User getAndCreateUser(String email);
}
