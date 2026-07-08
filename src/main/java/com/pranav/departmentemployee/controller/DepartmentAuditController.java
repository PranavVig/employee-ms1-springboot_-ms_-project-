package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.DepartmentAuditResponse;
import com.pranav.departmentemployee.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/audit")
@RequiredArgsConstructor
public class DepartmentAuditController {

    private final DepartmentService departmentService;

    @GetMapping
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
}