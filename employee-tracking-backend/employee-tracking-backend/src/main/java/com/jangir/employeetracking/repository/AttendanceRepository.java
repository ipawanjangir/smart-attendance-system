package com.jangir.employeetracking.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jangir.employeetracking.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    
}
