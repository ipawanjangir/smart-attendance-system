package com.jangir.employeetracking.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jangir.employeetracking.entity.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

    
}
