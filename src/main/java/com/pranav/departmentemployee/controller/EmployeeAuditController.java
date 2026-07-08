package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.EmployeeAuditResponse;
import com.pranav.departmentemployee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp/audit")
@RequiredArgsConstructor
public class EmployeeAuditController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeAuditResponse>> getEmployeeAudit(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "auditTimestamp") String sortBy,

            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                employeeService.getEmployeeAudit(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}