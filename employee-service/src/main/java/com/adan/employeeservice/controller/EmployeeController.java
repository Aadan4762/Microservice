package com.adan.employeeservice.controller;

import com.adan.employeeservice.dto.DepartmentResponse;
import com.adan.employeeservice.dto.EmployeeRequest;
import com.adan.employeeservice.dto.EmployeeResponse;
import com.adan.employeeservice.entity.Employee;
import com.adan.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
   // private final RestTemplate restTemplate;
    private final WebClient webClient;
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
        // Retrieve the employee response from the service
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        if (employeeResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        DepartmentResponse departmentResponse = webClient.get()
                .uri("http://localhost:8081/api/v2/department/" + employeeResponse.getDepartment())
                .retrieve()
                .bodyToMono(DepartmentResponse.class)
                .block();

        if (departmentResponse != null) {
            employeeResponse.setDepartment(departmentResponse.getName());
        }

        return ResponseEntity.ok(employeeResponse);
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
