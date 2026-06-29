package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.request.EmployeeRequest;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<EmployeeResponse> getEmployeeByName(
            @PathVariable String name) {

        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }
}
