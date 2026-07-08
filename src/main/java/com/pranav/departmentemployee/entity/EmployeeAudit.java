package com.pranav.departmentemployee.entity;

import com.pranav.departmentemployee.enums.AuditOperation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditOperation operation;

    @Column(nullable = false)
    private LocalDateTime auditTimestamp;

    private String oldEmpName;
    private String newEmpName;

    private String oldDepartmentName;
    private String newDepartmentName;

    private LocalDate oldJoiningDate;
    private LocalDate newJoiningDate;

    private String oldAddress;
    private String newAddress;
}