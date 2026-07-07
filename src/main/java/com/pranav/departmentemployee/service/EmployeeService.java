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

        employee.setEmpName(empName);
        employee.setDepartment(department);
        employee.setEmpJoiningDate(request.getEmpJoiningDate());
        employee.setAddress(request.getAddress());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy("admin");

        Employee updatedEmployee = employeePersistence.save(employee);

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
    }
}
