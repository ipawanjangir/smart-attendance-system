package com.jangir.employeetracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jangir.employeetracking.dto.CreateEmployeeRequest;
import com.jangir.employeetracking.entity.Employee;
import com.jangir.employeetracking.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee createEmployee(CreateEmployeeRequest request) {
        Employee emp = new Employee();
        emp.setEmpId(request.getEmpId());
        emp.setName(request.getName());
        emp.setUsername(request.getUsername());
        emp.setPassword(passwordEncoder.encode(request.getPassword())); // hash karke store
        emp.setBaseSalary(request.getBaseSalary());
        return employeeRepository.save(emp);
    }

    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }
}
