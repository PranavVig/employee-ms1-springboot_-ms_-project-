package com.pranav.departmentemployee.service;
import com.pranav.departmentemployee.exception.DepartmentAlreadyExistsException;
import com.pranav.departmentemployee.dto.response.DepartmentDetailsResponse;
import com.pranav.departmentemployee.exception.DepartmentHasEmployeesException;
import com.pranav.departmentemployee.exception.DepartmentNotFoundException;
import com.pranav.departmentemployee.repository.EmployeeRepository;
import com.pranav.departmentemployee.dto.request.DepartmentRequest;
import com.pranav.departmentemployee.dto.response.DepartmentResponse;
import com.pranav.departmentemployee.entity.Department;
import com.pranav.departmentemployee.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pranav.departmentemployee.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String deptName = request.getDeptName().trim();
        if (departmentRepository.existsByDeptName(deptName)) {
            throw new DepartmentAlreadyExistsException("Department already exists");
        }

        Department department = Department.builder()
                .deptName(deptName)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .updatedAt(LocalDateTime.now())
                .updatedBy("admin")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentResponse.builder()
                .deptId(savedDepartment.getDeptId())
                .deptName(savedDepartment.getDeptName())
                .build();
    }
    public Page<DepartmentResponse> getAllDepartments(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return departmentRepository.findAll(pageable)
                .map(department -> DepartmentResponse.builder()
                        .deptId(department.getDeptId())
                        .deptName(department.getDeptName())
                        .build());
    }
    public DepartmentDetailsResponse getDepartmentById(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        long employeeCount = employeeRepository.countByDepartmentDeptId(deptId);

        return DepartmentDetailsResponse.builder()
                .deptId(department.getDeptId())
                .deptName(department.getDeptName())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .employeeCount(employeeCount)
                .build();
    }
    public List<EmployeeResponse> getEmployeesByDepartment(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        return employeeRepository.findByDepartmentDeptId(deptId)
                .stream()
                .map(employee -> EmployeeResponse.builder()
                        .empId(employee.getEmpId())
                        .empName(employee.getEmpName())
                        .departmentName(department.getDeptName())
                        .empJoiningDate(employee.getEmpJoiningDate())
                        .address(employee.getAddress())
                        .build())
                .toList();
    }
    public DepartmentResponse updateDepartment(
            Long deptId,
            DepartmentRequest request) {
        String deptName = request.getDeptName().trim();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        if (departmentRepository.existsByDeptNameAndDeptIdNot(
                deptName,
                deptId)) {

            throw new DepartmentAlreadyExistsException(
                    "Department already exists");
        }

        department.setDeptName(deptName);
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy("admin");

        Department updatedDepartment =
                departmentRepository.save(department);

        return DepartmentResponse.builder()
                .deptId(updatedDepartment.getDeptId())
                .deptName(updatedDepartment.getDeptName())
                .build();
    }
    public void deleteDepartment(Long deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException("Department not found"));

        long employeeCount =
                employeeRepository.countByDepartmentDeptId(deptId);

        if (employeeCount > 0) {
            throw new DepartmentHasEmployeesException(
                    "Cannot delete department because employees are assigned to it.");
        }

        departmentRepository.delete(department);
    }
}
