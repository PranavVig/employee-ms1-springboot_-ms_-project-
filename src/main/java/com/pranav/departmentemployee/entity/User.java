package com.pranav.departmentemployee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedAttempts = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean accountLocked = false;

    private LocalDateTime lockTime;
}