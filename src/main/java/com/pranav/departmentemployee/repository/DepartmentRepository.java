package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
       SELECT COUNT(d) > 0
       FROM Department d
       WHERE LOWER(TRIM(d.deptName)) = LOWER(TRIM(:deptName))
       """)
    boolean existsByDeptName(@Param("deptName") String deptName);


    @Query("""
       SELECT COUNT(d) > 0
       FROM Department d
       WHERE LOWER(TRIM(d.deptName)) = LOWER(TRIM(:deptName))
       AND d.deptId <> :deptId
       """)
    boolean existsByDeptNameAndDeptIdNot(
            @Param("deptName") String deptName,
            @Param("deptId") Long deptId
    );

    @Query("""
       SELECT d
       FROM Department d
       WHERE LOWER(TRIM(d.deptName))
       LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))
       """)
    List<Department> searchDepartments(
            @Param("keyword") String keyword
    );
}
