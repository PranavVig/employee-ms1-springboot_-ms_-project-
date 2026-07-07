package com.pranav.departmentemployee.service.persistence;

import com.pranav.departmentemployee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeePersistence {

    Employee save(Employee employee);

    Page<Employee> findAll(Pageable pageable);

    Optional<Employee> findEmployeeByName(String name);

    Optional<Employee> findById(Long empId);

    void delete(Employee employee);

    List<Employee> searchEmployees(String keyword);
}