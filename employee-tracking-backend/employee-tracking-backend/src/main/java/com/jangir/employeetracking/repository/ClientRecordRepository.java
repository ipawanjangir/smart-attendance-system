package com.jangir.employeetracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jangir.employeetracking.entity.ClientRecord;

public interface ClientRecordRepository extends JpaRepository<ClientRecord, Long> {
    List<ClientRecord> findByEmployeeIdOrderByIdDesc(Long employeeId);
    List<ClientRecord> findAllByOrderByIdDesc();
}
