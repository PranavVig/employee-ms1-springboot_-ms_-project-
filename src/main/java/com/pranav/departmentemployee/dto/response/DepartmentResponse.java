package com.pranav.departmentemployee.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {

    private Long deptId;

    private String deptName;

}
