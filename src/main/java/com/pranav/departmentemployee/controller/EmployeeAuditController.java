package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.EmployeeAuditResponse;
import com.pranav.departmentemployee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/emp/audit")
@RequiredArgsConstructor
public class EmployeeAuditController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
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
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<byte[]> exportEmployeeAudit(

            @RequestParam String format) throws java.io.IOException {

        byte[] file = employeeService.exportEmployeeAudit(format);

        String fileName = "employee_audit." +
                ("csv".equalsIgnoreCase(format) ? "csv" : "xlsx");

        MediaType mediaType =
                "csv".equalsIgnoreCase(format)
                        ? MediaType.parseMediaType("text/csv")
                        : MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .contentType(mediaType)
                .body(file);
    }
}