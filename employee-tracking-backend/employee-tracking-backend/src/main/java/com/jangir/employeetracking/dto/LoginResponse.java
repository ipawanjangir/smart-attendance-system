package com.jangir.employeetracking.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String name;
    private String empId;

    public LoginResponse(String token, String role, String name, String empId) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.empId = empId;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmpId() { return empId; }
}
