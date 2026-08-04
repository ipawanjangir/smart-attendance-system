package com.jangir.employeetracking.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangir.employeetracking.entity.Attendance;
import com.jangir.employeetracking.entity.Employee;
import com.jangir.employeetracking.repository.AttendanceRepository;
import com.jangir.employeetracking.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. Employee Check-In (Selfie + Time)
    @PostMapping("/check-in/{empId}")
    public ResponseEntity<?> checkIn(@PathVariable Long empId, @RequestBody Map<String, String> body) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckInTime(LocalTime.now());
        attendance.setSelfieBase64(body.get("selfie")); // Base64 selfie image string

        attendanceRepository.save(attendance);
        return ResponseEntity.ok(Map.of("message", "Check-in successful", "attendance", attendance));
    }

    // 2. Employee Check-Out & Working Hours Calc
    @PostMapping("/check-out/{attendanceId}")
    public ResponseEntity<?> checkOut(@PathVariable Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        LocalTime checkOutTime = LocalTime.now();
        attendance.setCheckOutTime(checkOutTime);

        // Calculate total hours worked
        Duration duration = Duration.between(attendance.getCheckInTime(), checkOutTime);
        double hours = duration.toMinutes() / 60.0;
        attendance.setTotalWorkingHours(Math.round(hours * 100.0) / 100.0);

        attendanceRepository.save(attendance);
        return ResponseEntity.ok(Map.of("message", "Check-out successful", "attendance", attendance));
    }

    // 3. Admin View: All Attendance Reports
    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll());
    }
}