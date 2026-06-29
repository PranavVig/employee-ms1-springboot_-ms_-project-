package com.pranav.departmentemployee.cassandra.repository;

import com.pranav.departmentemployee.cassandra.entity.EmployeeCassandra;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;
import java.util.UUID;

public interface CassandraEmployeeRepository
        extends CassandraRepository<EmployeeCassandra, UUID> {

    Optional<EmployeeCassandra> findByEmpName(String empName);

}