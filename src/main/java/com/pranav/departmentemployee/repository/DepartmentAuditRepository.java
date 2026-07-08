package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.DepartmentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentAuditRepository
        extends JpaRepository<DepartmentAudit, Long> {
}