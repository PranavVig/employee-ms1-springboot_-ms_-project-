package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {



    @Query("""
       SELECT COUNT(d) > 0
       FROM Department d
       WHERE d.deptName = :deptName
       """)
    boolean existsByDeptName(@Param("deptName") String deptName);

}
