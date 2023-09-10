package com.adan.employeeservice.controller;

import com.adan.employeeservice.dto.DepartmentDto;
import com.adan.employeeservice.dto.EmployeeDto;
import com.adan.employeeservice.entity.Employee;
import com.adan.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RestTemplate restTemplate;

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        super();
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    public List <EmployeeDto> getAllEmployees(){
        return employeeService.getAllEmployees().stream().map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") int id){
        Employee employee = employeeService.getEmployeeById(id);
        EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
        DepartmentDto departmentDto = restTemplate.getForObject("http://localhost:8081/api/v2/department/{id}", DepartmentDto.class, id);
        employeeResponse.setDepartmentDto(departmentDto);
        return ResponseEntity.ok().body(employeeResponse);

    }
    @PostMapping("/create")
    public  ResponseEntity <EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto){
        Employee employeeRequest = modelMapper.map(employeeDto, Employee.class);
        Employee employee = employeeService.createEmployee(employeeRequest);
        EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
        return new ResponseEntity<EmployeeDto>(employeeResponse, HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable int id, @RequestBody EmployeeDto employeeDto) {
        Employee employeeRequest = modelMapper.map(employeeDto, Employee.class);
        Employee employee = employeeService.updateEmployee(id, employeeRequest);
        EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);
        return ResponseEntity.ok().body(employeeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") int id){
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<String>("Employee successfully deleted!", HttpStatus.OK);
    }


}
