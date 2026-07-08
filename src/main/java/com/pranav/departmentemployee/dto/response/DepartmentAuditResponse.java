package com.pranav.departmentemployee.dto.response;
import com.pranav.departmentemployee.enums.AuditOperation;
import lombok.*;
import org.apache.kafka.common.acl.AccessControlEntry;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAuditResponse {

    private AuditOperation operation;

    private LocalDateTime auditTimestamp;

    private String oldDeptName;

    private String newDeptName;
}