package com.adan.employeeservice.service;

import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.entity.Employee;
import com.adan.employeeservice.exception.ResourceNotFoundException;
import com.adan.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImplementation implements EmployeeService{


    private final EmployeeRepository employeeRepository;
    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .department(employeeRequest.getDepartment())
                .build();
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .build();
    }


    @Override
    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.map(this::mapToEmployeeResponse).orElse(null);
    }


    @Override
    public boolean deleteEmployeeById(int id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        employeeOptional.ifPresent(employee -> {
            employeeRepository.delete(employee);
            log.info("Employee{} is deleted", employee.getId());
        });
        return false;
    }

    @Override
    public boolean updateEmployeeById(EmployeeRequest employeeRequest, int id) {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);

        existingEmployeeOptional.ifPresent(existingEmployee -> {
            existingEmployee.setName(employeeRequest.getName());
            existingEmployee.setEmail(employeeRequest.getEmail());
            existingEmployee.setDepartment(employeeRequest.getDepartment());

            employeeRepository.save(existingEmployee);
        });
        return false;
    }
}