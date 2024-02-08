package com.rabobank.loans.domain;

import com.rabobank.loans.infrastructure.repositories.UserRepository;
import com.rabobank.loans.infrastructure.repositories.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServiceInterface {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getAndCreateUser(String email) {
        var queryResult = userRepository.findByEmail(email);
        if (queryResult.isEmpty()) {
            var newUser = new User();
            newUser.setEmail(email);
            return userRepository.save(newUser);
        }
        return queryResult.get();
    }
}
