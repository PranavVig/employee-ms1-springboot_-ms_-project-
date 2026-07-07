package com.pranav.departmentemployee.service;

import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.dto.response.SearchResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.entity.Employee;
import com.pranav.departmentemployee.repository.DepartmentRepository;
import com.pranav.departmentemployee.service.persistence.EmployeePersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DepartmentRepository departmentRepository;
    private final EmployeePersistence employeePersistence;
    public SearchResponse search(String keyword) {

        List<DepartmentResponse> departments =
                departmentRepository.searchDepartments(keyword)
                        .stream()
                        .map(department -> DepartmentResponse.builder()
                                .deptId(department.getDeptId())
                                .deptName(department.getDeptName())
                                .build())
                        .toList();

        List<EmployeeResponse> employees =
                employeePersistence.searchEmployees(keyword)
                        .stream()
                        .map(employee -> EmployeeResponse.builder()
                                .empId(employee.getEmpId())
                                .empName(employee.getEmpName())
                                .departmentName(employee.getDepartment().getDeptName())
                                .empJoiningDate(employee.getEmpJoiningDate())
                                .address(employee.getAddress())
                                .build())
                        .toList();

        return SearchResponse.builder()
                .departments(departments)
                .employees(employees)
                .build();
    }
}