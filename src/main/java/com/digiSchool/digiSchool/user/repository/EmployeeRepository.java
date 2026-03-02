package com.digiSchool.digiSchool.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digiSchool.digiSchool.user.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
