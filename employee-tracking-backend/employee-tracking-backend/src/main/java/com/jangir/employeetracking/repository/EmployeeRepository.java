package com.jangir.employeetracking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jangir.employeetracking.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByEmpId(String empId);
}
