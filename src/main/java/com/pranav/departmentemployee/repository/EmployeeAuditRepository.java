package com.pranav.departmentemployee.repository;

import com.pranav.departmentemployee.entity.EmployeeAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuditRepository
        extends JpaRepository<EmployeeAudit, Long> {
}