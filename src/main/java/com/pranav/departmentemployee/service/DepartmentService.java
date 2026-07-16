package com.pranav.departmentemployee.service;
import com.pranav.departmentemployee.exception.DepartmentAlreadyExistsException;
import com.pranav.departmentemployee.dto.response.DepartmentDetailsResponse;
import com.pranav.departmentemployee.exception.DepartmentHasEmployeesException;
import com.pranav.departmentemployee.exception.DepartmentNotFoundException;
import com.pranav.departmentemployee.repository.EmployeeRepository;
import com.pranav.departmentemployee.dto.request.DepartmentRequest;
import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.List;

import com.pranav.departmentemployee.entity.DepartmentAudit;
import com.pranav.departmentemployee.enums.AuditOperation;
import com.pranav.departmentemployee.repository.DepartmentAuditRepository;
import com.pranav.departmentemployee.dto.response.DepartmentAuditResponse;
import com.pranav.departmentemployee.entity.DepartmentAudit;

import com.pranav.departmentemployee.service.export.CsvExportService;
import com.pranav.departmentemployee.service.export.ExcelExportService;


@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentAuditRepository departmentAuditRepository;

    private final CsvExportService csvExportService;
    private final ExcelExportService excelExportService;

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String deptName = request.getDeptName().trim();
        if (departmentRepository.existsByDeptName(deptName)) {
            throw new DepartmentAlreadyExistsException("Department already exists");
        }

        Department department = Department.builder()
                .deptName(deptName)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();


        Department savedDepartment = departmentRepository.save(department);
        saveDepartmentAudit(
                savedDepartment,
                AuditOperation.CREATE,
                null,
                savedDepartment.getDeptName()
        );

        return DepartmentResponse.builder()
                .deptId(savedDepartment.getDeptId())
                .deptName(savedDepartment.getDeptName())
                .build();
    }
    public Page<DepartmentResponse> getAllDepartments(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return departmentRepository.findAll(pageable)
                .map(department -> DepartmentResponse.builder()
                        .deptId(department.getDeptId())
                        .deptName(department.getDeptName())
                        .build());
    }
    public Page<DepartmentAuditResponse> getDepartmentAudit(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return departmentAuditRepository.findAll(pageable)
                .map(audit -> DepartmentAuditResponse.builder()
                        .operation(audit.getOperation())
                        .auditTimestamp(audit.getAuditTimestamp())
                        .oldDeptName(audit.getOldDeptName())
                        .newDeptName(audit.getNewDeptName())
                        .build());
    }
    public DepartmentDetailsResponse getDepartmentById(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        long employeeCount = employeeRepository.countByDepartmentDeptId(deptId);

        return DepartmentDetailsResponse.builder()
                .deptId(department.getDeptId())
                .deptName(department.getDeptName())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .employeeCount(employeeCount)
                .build();
    }
    public List<EmployeeResponse> getEmployeesByDepartment(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        return employeeRepository.findByDepartmentDeptId(deptId)
                .stream()
                .map(employee -> EmployeeResponse.builder()
                        .empId(employee.getEmpId())
                        .empName(employee.getEmpName())
                        .departmentName(department.getDeptName())
                        .empJoiningDate(employee.getEmpJoiningDate())
                        .address(employee.getAddress())
                        .build())
                .toList();
    }
    public DepartmentResponse updateDepartment(
            Long deptId,
            DepartmentRequest request) {
        String deptName = request.getDeptName().trim();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        if (departmentRepository.existsByDeptNameAndDeptIdNot(
                deptName,
                deptId)) {

            throw new DepartmentAlreadyExistsException(
                    "Department already exists");
        }
        String oldDeptName = department.getDeptName();
        department.setDeptName(deptName);
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy("admin");

        Department updatedDepartment =
                departmentRepository.save(department);
        saveDepartmentAudit(
                updatedDepartment,
                AuditOperation.UPDATE,
                oldDeptName,
                updatedDepartment.getDeptName()
        );

        return DepartmentResponse.builder()
                .deptId(updatedDepartment.getDeptId())
                .deptName(updatedDepartment.getDeptName())
                .build();
    }
    public void deleteDepartment(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        long employeeCount =
                employeeRepository.countByDepartmentDeptId(deptId);

        if (employeeCount > 0) {
            throw new DepartmentHasEmployeesException(
                    "Cannot delete department because employees are assigned to it.");
        }

        departmentRepository.delete(department);
        saveDepartmentAudit(
                department,
                AuditOperation.DELETE,
                department.getDeptName(),
                null
        );
    }
    public byte[] exportDepartmentAudit(String format) throws java.io.IOException {

        List<DepartmentAudit> audits = departmentAuditRepository.findAll();

        List<String> headers = List.of(
                "Audit ID",
                "Department ID",
                "Operation",
                "Timestamp",
                "Old Department",
                "New Department"
        );

        List<List<String>> rows = audits.stream()
                .map(audit -> List.of(
                        String.valueOf(audit.getAuditId()),
                        String.valueOf(audit.getDepartmentId()),
                        audit.getOperation().name(),
                        audit.getAuditTimestamp().toString(),
                        audit.getOldDeptName() == null ? "" : audit.getOldDeptName(),
                        audit.getNewDeptName() == null ? "" : audit.getNewDeptName()
                ))
                .toList();

        if ("csv".equalsIgnoreCase(format)) {
            return csvExportService.generateCsv(headers, rows);
        }

        if ("xlsx".equalsIgnoreCase(format)) {
            return excelExportService.generateExcel(headers, rows);
        }

        throw new IllegalArgumentException("Unsupported export format.");
    }
    private void saveDepartmentAudit(
            Department department,
            AuditOperation operation,
            String oldDeptName,
            String newDeptName) {

        DepartmentAudit audit = DepartmentAudit.builder()
                .departmentId(department.getDeptId())
                .operation(operation)
                .auditTimestamp(LocalDateTime.now())
                .oldDeptName(oldDeptName)
                .newDeptName(newDeptName)
                .build();

        departmentAuditRepository.save(audit);
    }
}
