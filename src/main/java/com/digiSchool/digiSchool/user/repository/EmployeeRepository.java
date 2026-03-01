package com.digiSchool.digiSchool.user.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.user.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    
}
