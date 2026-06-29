package com.pranav.departmentemployee.cassandra.persistence;

import com.pranav.departmentemployee.cassandra.entity.EmployeeCassandra;
import com.pranav.departmentemployee.cassandra.repository.CassandraEmployeeRepository;
import com.pranav.departmentemployee.entity.Employee;
import com.pranav.departmentemployee.service.persistence.EmployeePersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.database", havingValue = "cassandra")
public class CassandraEmployeePersistence implements EmployeePersistence {

    private final CassandraEmployeeRepository repository;

    @Override
    public Employee save(Employee employee) {

        EmployeeCassandra cassandraEmployee = EmployeeCassandra.builder()
                .empId(UUID.randomUUID())
                .empName(employee.getEmpName())
                .departmentName(employee.getDepartment().getDeptName())
                .empJoiningDate(employee.getEmpJoiningDate())
                .address(employee.getAddress())
                .createdAt(employee.getCreatedAt())
                .createdBy(employee.getCreatedBy())
                .updatedAt(employee.getUpdatedAt())
                .updatedBy(employee.getUpdatedBy())
                .build();

        repository.save(cassandraEmployee);

        return employee;
    }
    @Override
    public Page<Employee> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Employee> findEmployeeByName(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
