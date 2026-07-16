package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.DepartmentAuditResponse;
import com.pranav.departmentemployee.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/department/audit")
@RequiredArgsConstructor
public class DepartmentAuditController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Page<DepartmentAuditResponse>> getDepartmentAudit(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "auditTimestamp") String sortBy,

            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                departmentService.getDepartmentAudit(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );
    }
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<byte[]> exportDepartmentAudit(

            @RequestParam String format) throws java.io.IOException {

        byte[] file = departmentService.exportDepartmentAudit(format);

        String fileName = "department_audit." +
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

