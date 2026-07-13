package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.request.DepartmentRequest;
import com.pranav.departmentemployee.dto.response.DepartmentDetailsResponse;
import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response =
                departmentService.createDepartment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartments(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "deptName") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                departmentService.getAllDepartments(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/{deptId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<DepartmentDetailsResponse> getDepartmentById(
            @PathVariable Long deptId) {

        return ResponseEntity.ok(
                departmentService.getDepartmentById(deptId)
        );
    }

    @GetMapping("/{deptId}/employees")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(
            @PathVariable Long deptId) {

        return ResponseEntity.ok(
                departmentService.getEmployeesByDepartment(deptId)
        );
    }

    @PutMapping("/{deptId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long deptId,
            @Valid @RequestBody DepartmentRequest request) {

        return ResponseEntity.ok(
                departmentService.updateDepartment(deptId, request)
        );
    }

    @DeleteMapping("/{deptId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long deptId) {

        departmentService.deleteDepartment(deptId);

        return ResponseEntity.noContent().build();
    }
}