package com.adan.employeeservice.service;


import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface EmployeeService {

    void createEmployee(EmployeeRequest employeeRequest);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(int id);
    boolean deleteEmployeeById(int id);
    boolean updateEmployeeById(EmployeeRequest employeeRequest, int id);
}
