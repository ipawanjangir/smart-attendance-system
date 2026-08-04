package com.jangir.employeetracking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangir.employeetracking.dto.ClientRecordRequest;
import com.jangir.employeetracking.entity.ClientRecord;
import com.jangir.employeetracking.service.ClientRecordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/client-records")
public class ClientRecordController {

    @Autowired
    private ClientRecordService clientRecordService;

    // Employee apna record submit karta hai. Authentication object se
    // username milta hai (JwtAuthFilter ne set kiya hota hai) — isse
    // hume pata chal jaata hai ye record kis employee ka hai, bina
    // frontend se employee ID mangwaye (jyada secure — employee kisi
    // aur ke naam se record nahi daal sakta).
    @PostMapping
    public ClientRecord createRecord(@Valid @RequestBody ClientRecordRequest request, Authentication auth) {
        return clientRecordService.createRecord(auth.getName(), request);
    }

    // Employee ke apne records — "My Work Records" table ke liye
    @GetMapping("/my")
    public List<ClientRecord> myRecords(Authentication auth) {
        return clientRecordService.myRecords(auth.getName());
    }

    // Admin ke liye — sabke records ek saath
    @GetMapping
    public List<ClientRecord> allRecords() {
        return clientRecordService.allRecords();
    }
}
