package com.pranav.departmentemployee.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class DepartmentDetailsResponse {

    private Long deptId;

    private String deptName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long employeeCount;
}