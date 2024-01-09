package com.adan.employeeservice.controller;

import com.adan.employeeservice.dto.APIResponse;
import com.adan.employeeservice.dto.DepartmentResponse;
import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.entity.Employee;
import com.adan.employeeservice.service.EmployeeService;
import com.adan.employeeservice.service.EmployeeServiceImplementation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeServiceImplementation employeeServiceImplementation;
    @Autowired
    private final RestTemplate restTemplate;
    private static final String EMPLOYEE_SERVICE = "employee-Service";

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        try {
            employeeService.createEmployee(employeeRequest);
            return "Employee created successfully";
        } catch (Exception exception) {
            return "Failed to create employee" + exception.getMessage();
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "employeeServiceFallback")
    @ResponseStatus(HttpStatus.OK)
    public Object getEmployeeById(@Valid @PathVariable int id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);

        if (employee != null) {
            ResponseEntity<DepartmentResponse> responseEntity = restTemplate.getForEntity(
                    "http://localhost:8081/api/v2/department/" + employee.getId(), DepartmentResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                employee.setDepartment(String.valueOf(responseEntity.getBody()));
            }

            return employee;
        } else {
            return "Employee not found";
        }
    }
    public Object employeeServiceFallback(Exception e) {
        return "This is a fallback method for Employee Service";
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateEmployeeById(@PathVariable int id, @RequestBody EmployeeRequest employeeRequest){
        boolean isUpdated = employeeService.updateEmployeeById( employeeRequest, id);
        if (isUpdated){
            return "Employee updated successfully";
        }else {
            return "Employee did not update";
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEmployeeById(@PathVariable int id){
        boolean isDeleted = employeeService.deleteEmployeeById(id);

        if (isDeleted){
            return "Employee deleted successfully";
        }else {
            return "Employee could not be found";
        }
    }

    @GetMapping("/{field}")
    private APIResponse<List<Employee>> getEmployeesWithSort(@PathVariable String field) {
        List<Employee> allEmployees = employeeServiceImplementation.findEmployeesWithSorting(field);
        return new APIResponse<>(allEmployees.size(), allEmployees);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    private APIResponse<Page<Employee>> getEmployeesWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Employee> employeesWithPagination = employeeServiceImplementation.findEmployeesWithPagination(offset, pageSize);
        return new APIResponse<>(employeesWithPagination.getSize(), employeesWithPagination);
    }

    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
    private APIResponse<Page<Employee>> getEmployeesWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        Page<Employee> employeesWithPagination = employeeServiceImplementation.findEmployeesWithPaginationAndSorting(offset, pageSize, field);
        return new APIResponse<>(employeesWithPagination.getSize(), employeesWithPagination);
    }

}