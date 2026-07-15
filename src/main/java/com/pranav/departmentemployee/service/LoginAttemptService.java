package com.pranav.departmentemployee.service;

import com.pranav.departmentemployee.entity.User;
import com.pranav.departmentemployee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MINUTES = 1;

    private final UserRepository userRepository;

    public void loginSucceeded(User user) {

        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);

        userRepository.save(user);
    }

    public void loginFailed(User user) {

        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    public boolean unlockIfLockExpired(User user) {

        if (!Boolean.TRUE.equals(user.getAccountLocked())) {
            return true;
        }

        LocalDateTime unlockTime =
                user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);

        if (LocalDateTime.now().isAfter(unlockTime)) {

            user.setAccountLocked(false);
            user.setFailedAttempts(0);
            user.setLockTime(null);

            userRepository.save(user);

            return true;
        }

        return false;
    }
    public long getRemainingLockTime(User user) {

        if (!Boolean.TRUE.equals(user.getAccountLocked())
                || user.getLockTime() == null) {
            return 0;
        }

        LocalDateTime unlockTime =
                user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);

        long remainingSeconds =
                Duration.between(LocalDateTime.now(), unlockTime).getSeconds();

        return Math.max(remainingSeconds, 0);
    }
}