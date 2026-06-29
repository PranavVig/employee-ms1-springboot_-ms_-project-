package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.empName = :name
            """)
    Optional<Employee> findEmployeeByName(@Param("name") String name);

}
