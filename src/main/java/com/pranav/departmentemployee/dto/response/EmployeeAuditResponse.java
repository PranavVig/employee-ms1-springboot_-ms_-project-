package com.pranav.departmentemployee.dto.response;
import com.pranav.departmentemployee.enums.AuditOperation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAuditResponse {
    private AuditOperation operation;

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
