package com.pranav.departmentemployee.service;
import com.pranav.departmentemployee.exception.DepartmentNotFoundException;
import com.pranav.departmentemployee.dto.request.EmployeeRequest;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.entity.Employee;
import com.pranav.departmentemployee.exception.EmployeeNotFoundException;
import com.pranav.departmentemployee.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.pranav.departmentemployee.service.persistence.EmployeePersistence;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;
import com.pranav.departmentemployee.kafka.producer.EmployeeProducer;
import com.pranav.departmentemployee.event.EmployeeCreatedEvent;

import com.pranav.departmentemployee.entity.EmployeeAudit;
import com.pranav.departmentemployee.enums.AuditOperation;
import com.pranav.departmentemployee.repository.EmployeeAuditRepository;
import java.time.LocalDate;
import com.pranav.departmentemployee.dto.response.EmployeeAuditResponse;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeePersistence employeePersistence;
    private final DepartmentRepository departmentRepository;
    private final EmployeeProducer employeeProducer;
    private final EmployeeAuditRepository employeeAuditRepository;

    public EmployeeResponse createEmployee(EmployeeRequest request) {

        Department department = departmentRepository.findById(request.getDeptId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        Employee employee = Employee.builder()
                .department(department)
                .empName(request.getEmpName())
                .empJoiningDate(request.getEmpJoiningDate())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        Employee savedEmployee = employeePersistence.save(employee);

        saveEmployeeAudit(
                savedEmployee,
                AuditOperation.CREATE,
                null,
                savedEmployee.getEmpName(),
                null,
                savedEmployee.getDepartment().getDeptName(),
                null,
                savedEmployee.getEmpJoiningDate(),
                null,
                savedEmployee.getAddress()
        );

        EmployeeCreatedEvent event = EmployeeCreatedEvent.builder()
                .employeeId(savedEmployee.getEmpId())
                .employeeName(savedEmployee.getEmpName())
                .address(savedEmployee.getAddress())
                .build();

        employeeProducer.publishEmployeeCreatedEvent(event);
        return EmployeeResponse.builder()
                .empId(savedEmployee.getEmpId())
                .empName(savedEmployee.getEmpName())
                .departmentName(savedEmployee.getDepartment().getDeptName())
                .empJoiningDate(savedEmployee.getEmpJoiningDate())
                .address(savedEmployee.getAddress())
                .build();
    }

    public Page<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeePersistence.findAll(pageable)
                .map(employee -> EmployeeResponse.builder()
                        .empId(employee.getEmpId())
                        .empName(employee.getEmpName())
                        .departmentName(employee.getDepartment().getDeptName())
                        .empJoiningDate(employee.getEmpJoiningDate())
                        .address(employee.getAddress())
                        .build());
    }
    public Page<EmployeeAuditResponse> getEmployeeAudit(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeAuditRepository.findAll(pageable)
                .map(audit -> EmployeeAuditResponse.builder()
                        .operation(audit.getOperation())
                        .auditTimestamp(audit.getAuditTimestamp())
                        .oldEmpName(audit.getOldEmpName())
                        .newEmpName(audit.getNewEmpName())
                        .oldDepartmentName(audit.getOldDepartmentName())
                        .newDepartmentName(audit.getNewDepartmentName())
                        .oldJoiningDate(audit.getOldJoiningDate())
                        .newJoiningDate(audit.getNewJoiningDate())
                        .oldAddress(audit.getOldAddress())
                        .newAddress(audit.getNewAddress())
                        .build());
    }
    public EmployeeResponse getEmployeeByName(String name) {

        Employee employee = employeePersistence.findEmployeeByName(name)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        return EmployeeResponse.builder()
                .empId(employee.getEmpId())
                .empName(employee.getEmpName())
                .departmentName(employee.getDepartment().getDeptName())
                .empJoiningDate(employee.getEmpJoiningDate())
                .address(employee.getAddress())
                .build();
    }
    public EmployeeResponse getEmployeeById(Long empId) {

        Employee employee = employeePersistence.findById(empId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        return EmployeeResponse.builder()
                .empId(employee.getEmpId())
                .empName(employee.getEmpName().trim())
                .departmentName(employee.getDepartment().getDeptName())
                .empJoiningDate(employee.getEmpJoiningDate())
                .address(employee.getAddress())
                .build();
    }
    public EmployeeResponse updateEmployee(
            Long empId,
            EmployeeRequest request) {

        String empName = request.getEmpName().trim();

        Employee employee = employeePersistence.findById(empId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        Department department = departmentRepository.findById(request.getDeptId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        String oldEmpName = employee.getEmpName();
        String oldDepartmentName = employee.getDepartment().getDeptName();
        LocalDate oldJoiningDate = employee.getEmpJoiningDate();
        String oldAddress = employee.getAddress();

        employee.setEmpName(empName);
        employee.setDepartment(department);
        employee.setEmpJoiningDate(request.getEmpJoiningDate());
        employee.setAddress(request.getAddress());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy("admin");

        Employee updatedEmployee = employeePersistence.save(employee);
        saveEmployeeAudit(
                updatedEmployee,
                AuditOperation.UPDATE,
                oldEmpName,
                updatedEmployee.getEmpName(),
                oldDepartmentName,
                updatedEmployee.getDepartment().getDeptName(),
                oldJoiningDate,
                updatedEmployee.getEmpJoiningDate(),
                oldAddress,
                updatedEmployee.getAddress()
        );
        return EmployeeResponse.builder()
                .empId(updatedEmployee.getEmpId())
                .empName(updatedEmployee.getEmpName())
                .departmentName(updatedEmployee.getDepartment().getDeptName())
                .empJoiningDate(updatedEmployee.getEmpJoiningDate())
                .address(updatedEmployee.getAddress())
                .build();
    }
    public void deleteEmployee(Long empId) {

        Employee employee = employeePersistence.findById(empId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        employeePersistence.delete(employee);
        saveEmployeeAudit(
                employee,
                AuditOperation.DELETE,
                employee.getEmpName(),
                null,
                employee.getDepartment().getDeptName(),
                null,
                employee.getEmpJoiningDate(),
                null,
                employee.getAddress(),
                null
        );
    }
    private void saveEmployeeAudit(
            Employee employee,
            AuditOperation operation,
            String oldEmpName,
            String newEmpName,
            String oldDepartmentName,
            String newDepartmentName,
            LocalDate oldJoiningDate,
            LocalDate newJoiningDate,
            String oldAddress,
            String newAddress) {

        EmployeeAudit audit = EmployeeAudit.builder()
                .employeeId(employee.getEmpId())
                .operation(operation)
                .auditTimestamp(LocalDateTime.now())
                .oldEmpName(oldEmpName)
                .newEmpName(newEmpName)
                .oldDepartmentName(oldDepartmentName)
                .newDepartmentName(newDepartmentName)
                .oldJoiningDate(oldJoiningDate)
                .newJoiningDate(newJoiningDate)
                .oldAddress(oldAddress)
                .newAddress(newAddress)
                .build();

        employeeAuditRepository.save(audit);
    }
}
