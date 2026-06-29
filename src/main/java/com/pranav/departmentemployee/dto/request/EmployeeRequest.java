package com.pranav.departmentemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {

    @NotNull(message = "Department Id is required")
    private Long deptId;

    @NotBlank(message = "Employee name is required")
    private String empName;

    @NotNull(message = "Joining date is required")
    private LocalDate empJoiningDate;

    @NotBlank(message = "Address is required")
    private String address;
}