package com.adan.employeeservice.controller;

import com.adan.employeeservice.dto.DepartmentResponse;
import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final RestTemplate restTemplate;
    private static final String EMPLOYEE_SERVICE = "employee-Service";

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        employeeService.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "employeeServiceFallback")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getEmployeeById(@Valid @PathVariable int id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);

        if (employee != null) {
            ResponseEntity<DepartmentResponse> responseEntity = restTemplate.getForEntity(
                    "http://localhost:8081/api/v2/department/" + employee.getId(), DepartmentResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                DepartmentResponse departmentResponse = responseEntity.getBody();
                if (departmentResponse != null) {
                    employee.setDepartment(departmentResponse.getName());
                }
            }

            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

    }

    // Fallback method
    public ResponseEntity<Object> employeeServiceFallback(int id, Throwable throwable) {
        // Handle fallback logic, e.g., returning a default response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Employee service is unavailable");
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateEmployeeById(@PathVariable int id, @RequestBody EmployeeRequest employeeRequest) {
        boolean isUpdated = employeeService.updateEmployeeById(employeeRequest, id);
        if (isUpdated) {
            return ResponseEntity.ok("Employee updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteEmployeeById(@PathVariable int id) {
        boolean isDeleted = employeeService.deleteEmployeeById(id);

        if (isDeleted) {
            return ResponseEntity.ok("Employee deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }
}
