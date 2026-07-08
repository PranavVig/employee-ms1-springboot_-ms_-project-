package com.pranav.departmentemployee.entity;

import com.pranav.departmentemployee.enums.AuditOperation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "department_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    private Long departmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditOperation operation;

    @Column(nullable = false)
    private LocalDateTime auditTimestamp;

    private String oldDeptName;

    private String newDeptName;
}