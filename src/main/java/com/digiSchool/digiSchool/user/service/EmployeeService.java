package com.digiSchool.digiSchool.user.service;

import java.util.List;

import com.digiSchool.digiSchool.user.dto.EmployeeDTO;
import com.digiSchool.digiSchool.user.dto.EmployeeResponseDTO;
import com.digiSchool.digiSchool.user.model.Employee;

public interface EmployeeService {

	  // 🔹 Le tenant est récupéré automatiquement via TenantContext

	    Employee create(EmployeeDTO dto);

    EmployeeDTO update(Long id, EmployeeDTO dto);

    EmployeeDTO getById(Long id);

    List<EmployeeDTO> getAll();

    void delete(Long id);
}
