package com.isa.service;

import com.isa.domain.model.User;
import com.isa.domain.model.VerificationToken;
import com.isa.repository.UserRepository;
import com.isa.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository,
                                    UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VerificationToken createToken(User user) {
        verificationTokenRepository.findByUserId(user.getId())
                .ifPresent(verificationTokenRepository::delete);

        final VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(24, ChronoUnit.HOURS));
        return verificationTokenRepository.save(token);
    }

    @Transactional
    public void verifyToken(String tokenValue) {
        final VerificationToken token = verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token."));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            verificationTokenRepository.delete(token);
            throw new IllegalArgumentException(
                    "Verification token has expired. Please request a new one.");
        }

        final User user = token.getUser();
        if (Boolean.TRUE.equals(user.getVerified())) {
            verificationTokenRepository.delete(token);
            throw new IllegalArgumentException("Email address is already verified.");
        }

        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(token);
    }
}
