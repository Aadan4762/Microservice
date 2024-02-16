package com.adan.employeeservice.service;

import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    void createEmployee(EmployeeRequest employeeRequest);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(int id);
    boolean deleteEmployeeById(int id);
    boolean updateEmployeeById(EmployeeRequest employeeRequest, int id);
    List<Employee> findEmployeesWithSorting(String field);
    Page<Employee> findEmployeesWithPagination(int offset, int pageSize);
    Page<Employee> findEmployeesWithPaginationAndSorting(int offset,int pageSize,String field);
}

