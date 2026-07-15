package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.CurrentUserResponse;
import com.pranav.departmentemployee.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.pranav.departmentemployee.dto.response.LockStatusResponse;
import com.pranav.departmentemployee.entity.User;
import com.pranav.departmentemployee.repository.UserRepository;
import com.pranav.departmentemployee.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CurrentUserResponse response = CurrentUserResponse.builder()
                .username(userDetails.getUsername())
                .role(userDetails.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority())
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/lock-status")
    public ResponseEntity<LockStatusResponse> getLockStatus(
            @RequestParam String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !Boolean.TRUE.equals(user.getAccountLocked())) {

            return ResponseEntity.ok(
                    LockStatusResponse.builder()
                            .locked(false)
                            .remainingSeconds(0)
                            .build()
            );
        }

        return ResponseEntity.ok(
                LockStatusResponse.builder()
                        .locked(true)
                        .remainingSeconds(
                                loginAttemptService.getRemainingLockTime(user)
                        )
                        .build()
        );
    }
}