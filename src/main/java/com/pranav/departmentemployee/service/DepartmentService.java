package com.pranav.departmentemployee.service;
import com.pranav.departmentemployee.exception.DepartmentAlreadyExistsException;

import com.pranav.departmentemployee.dto.request.DepartmentRequest;
import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponse createDepartment(DepartmentRequest request) {

        if (departmentRepository.existsByDeptName(request.getDeptName())) {
            throw new DepartmentAlreadyExistsException("Department already exists");
        }

        Department department = Department.builder()
                .deptName(request.getDeptName())
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentResponse.builder()
                .deptId(savedDepartment.getDeptId())
                .deptName(savedDepartment.getDeptName())
                .build();
    }
}
