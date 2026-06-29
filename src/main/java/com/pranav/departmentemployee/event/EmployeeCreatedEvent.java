package com.pranav.departmentemployee.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreatedEvent {

    private Long employeeId;

    private String employeeName;

    private String address;
}
