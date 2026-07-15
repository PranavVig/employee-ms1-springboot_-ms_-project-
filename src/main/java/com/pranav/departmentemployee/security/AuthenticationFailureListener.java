package com.pranav.departmentemployee.security;

import com.pranav.departmentemployee.entity.User;
import com.pranav.departmentemployee.repository.UserRepository;
import com.pranav.departmentemployee.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {

        String username = event.getAuthentication().getName();

        userRepository.findByUsername(username)
                .ifPresent(loginAttemptService::loginFailed);
    }
}