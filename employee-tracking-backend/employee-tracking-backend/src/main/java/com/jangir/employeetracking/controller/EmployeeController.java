package com.jangir.employeetracking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangir.employeetracking.dto.CreateEmployeeRequest;
import com.jangir.employeetracking.entity.Employee;
import com.jangir.employeetracking.service.EmployeeService;

import jakarta.validation.Valid;

/**
 * Sirf ADMIN access kar sakta hai (SecurityConfig mein
 * /api/employees/** ko hasRole("ADMIN") se protect kiya hai).
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }

    @GetMapping
    public List<Employee> listEmployees() {
        return employeeService.listEmployees();
    }
}
