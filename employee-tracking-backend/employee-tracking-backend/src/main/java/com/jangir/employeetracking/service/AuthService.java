package com.jangir.employeetracking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jangir.employeetracking.dto.LoginRequest;
import com.jangir.employeetracking.dto.LoginResponse;
import com.jangir.employeetracking.entity.Employee;
import com.jangir.employeetracking.repository.EmployeeRepository;
import com.jangir.employeetracking.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Admin ka ek hi fixed account hai — database mein table banane ki
    // zaroorat nahi. Credentials application.properties se aate hain
    // (environment variable se override ho sakte hain, jaise DB password).
    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    public LoginResponse adminLogin(LoginRequest request) {
        if (!adminUsername.equals(request.getUsername()) || !adminPassword.equals(request.getPassword())) {
            throw new RuntimeException("Invalid admin username or password");
        }
        String token = jwtUtil.generateToken(adminUsername, "ADMIN");
        return new LoginResponse(token, "ADMIN", "Admin", null);
    }

    public LoginResponse employeeLogin(LoginRequest request) {
        Optional<Employee> empOpt = employeeRepository.findByUsername(request.getUsername());

        if (empOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        Employee emp = empOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), emp.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(emp.getUsername(), "EMPLOYEE");
        return new LoginResponse(token, "EMPLOYEE", emp.getName(), emp.getEmpId());
    }
}
