package com.pranav.departmentemployee.service.persistence;

import com.pranav.departmentemployee.entity.Employee;
import com.pranav.departmentemployee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.database", havingValue = "mysql")
public class MySqlEmployeePersistence implements EmployeePersistence {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Optional<Employee> findEmployeeByName(String name) {
        return employeeRepository.findEmployeeByName(name);
    }
}
