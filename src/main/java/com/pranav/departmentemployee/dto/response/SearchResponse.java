package com.pranav.departmentemployee.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {

    private List<DepartmentResponse> departments;

    private List<EmployeeResponse> employees;
}
