package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthTransactionService {

    private final UserRepository userRepository;

    public AuthTransactionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increasePasswordErrorCount(User user) {
        user.updatePasswordErrorCount(user.getPasswordErrorCount() + 1);
        userRepository.save(user);
    }
}