package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.request.DepartmentRequest;
import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response =
                departmentService.createDepartment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}