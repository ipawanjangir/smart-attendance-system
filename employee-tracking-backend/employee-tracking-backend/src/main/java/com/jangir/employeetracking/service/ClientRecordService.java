package com.jangir.employeetracking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jangir.employeetracking.dto.ClientRecordRequest;
import com.jangir.employeetracking.entity.ClientRecord;
import com.jangir.employeetracking.entity.Employee;
import com.jangir.employeetracking.repository.ClientRecordRepository;
import com.jangir.employeetracking.repository.EmployeeRepository;

@Service
public class ClientRecordService {

    @Autowired
    private ClientRecordRepository clientRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ClientRecord createRecord(String username, ClientRecordRequest request) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ClientRecord record = new ClientRecord();
        record.setEmployee(employee);
        record.setClientName(request.getClientName());
        record.setBusinessName(request.getBusinessName());
        record.setMobileNumber(request.getMobileNumber());
        record.setRequirement(request.getRequirement());
        record.setRemarks(request.getRemarks());
        record.setCallDate(request.getCallDate() != null ? request.getCallDate() : LocalDate.now());
        record.setFollowUpDate(request.getFollowUpDate());
        record.setEntryType(request.getEntryType() != null ? request.getEntryType() : "TODAY");

        return clientRecordRepository.save(record);
    }

    public List<ClientRecord> myRecords(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return clientRecordRepository.findByEmployeeIdOrderByIdDesc(employee.getId());
    }

    public List<ClientRecord> allRecords() {
        return clientRecordRepository.findAllByOrderByIdDesc();
    }
}
