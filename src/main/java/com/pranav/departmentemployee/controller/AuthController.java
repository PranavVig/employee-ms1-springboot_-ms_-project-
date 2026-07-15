package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.CurrentUserResponse;
import com.pranav.departmentemployee.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
}