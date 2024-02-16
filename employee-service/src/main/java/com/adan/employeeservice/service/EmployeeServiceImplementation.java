package com.adan.employeeservice.service;

import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.entity.Employee;
import com.adan.employeeservice.exception.ResourceNotFoundException;
import com.adan.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setDepartment(employeeRequest.getDepartment());
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToEmployeeResponse(employee);
    }

    @Override
    public boolean deleteEmployeeById(int id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employeeRepository.delete(employee);
            log.info("Employee {} is deleted", employee.getId());
            return true;
        } else {
            log.warn("Employee with id {} not found for deletion", id);
            return false;
        }
    }

    @Override
    public boolean updateEmployeeById(EmployeeRequest employeeRequest, int id) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setName(employeeRequest.getName());
                    existingEmployee.setEmail(employeeRequest.getEmail());
                    existingEmployee.setDepartment(employeeRequest.getDepartment());
                    employeeRepository.save(existingEmployee);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Employee> findEmployeesWithSorting(String field) {
        return employeeRepository.findAll(Sort.by(field));
    }

    @Override
    public Page<Employee> findEmployeesWithPagination(int offset, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public Page<Employee> findEmployeesWithPaginationAndSorting(int offset, int pageSize, String field) {
        return employeeRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(field)));
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .build();
    }
}
