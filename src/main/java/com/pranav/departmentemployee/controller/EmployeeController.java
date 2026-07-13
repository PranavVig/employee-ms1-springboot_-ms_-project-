package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.request.EmployeeRequest;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "empName") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<EmployeeResponse> getEmployeeByName(
            @PathVariable String name) {

        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }

    @GetMapping("/{empId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long empId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(empId)
        );
    }

    @PutMapping("/{empId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long empId,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(empId, request)
        );
    }

    @DeleteMapping("/{empId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long empId) {

        employeeService.deleteEmployee(empId);

        return ResponseEntity.noContent().build();
    }
}