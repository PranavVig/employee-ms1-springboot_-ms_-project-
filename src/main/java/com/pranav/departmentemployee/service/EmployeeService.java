package com.pranav.departmentemployee.service;
import com.pranav.departmentemployee.exception.DepartmentNotFoundException;
import com.pranav.departmentemployee.dto.request.EmployeeRequest;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.entity.Employee;
import com.pranav.departmentemployee.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.pranav.departmentemployee.service.persistence.EmployeePersistence;
import java.time.LocalDateTime;
import com.pranav.departmentemployee.repository.EmployeeRepository;

import com.pranav.departmentemployee.kafka.producer.EmployeeProducer;
import com.pranav.departmentemployee.event.EmployeeCreatedEvent;



@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeePersistence employeePersistence;
    private final DepartmentRepository departmentRepository;
    private final EmployeeProducer employeeProducer;

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

    public Page<EmployeeResponse> getAllEmployees(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return employeePersistence.findAll(pageable)
                .map(employee -> EmployeeResponse.builder()
                        .empId(employee.getEmpId())
                        .empName(employee.getEmpName())
                        .departmentName(employee.getDepartment().getDeptName())
                        .empJoiningDate(employee.getEmpJoiningDate())
                        .address(employee.getAddress())
                        .build());
    }
    public EmployeeResponse getEmployeeByName(String name) {

        Employee employee = employeePersistence.findEmployeeByName(name)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return EmployeeResponse.builder()
                .empId(employee.getEmpId())
                .empName(employee.getEmpName())
                .departmentName(employee.getDepartment().getDeptName())
                .empJoiningDate(employee.getEmpJoiningDate())
                .address(employee.getAddress())
                .build();
    }
}
