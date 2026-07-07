package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentDeptId(Long deptId);
    long countByDepartmentDeptId(Long deptId);
    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.empName = :name
            """)

    Optional<Employee> findEmployeeByName(@Param("name") String name);

    @Query("""
        SELECT e
        FROM Employee e
        WHERE LOWER(TRIM(e.empName))
        LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))
        """)
    List<Employee> searchEmployees(@Param("keyword") String keyword);
}
