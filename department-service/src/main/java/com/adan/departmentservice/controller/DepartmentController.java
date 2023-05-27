package com.adan.departmentservice.controller;

import com.adan.departmentservice.dto.DepartmentDto;
import com.adan.departmentservice.model.Department;
import com.adan.departmentservice.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/department")
public class DepartmentController {

    @Autowired
    private ModelMapper modelMapper;
    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        super();
        this.departmentService = departmentService;
    }

    @GetMapping("/all")
   public List<DepartmentDto>getListOfDepartment(){
        return departmentService.getAlldepartment().stream().map(department -> modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
   public ResponseEntity  <DepartmentDto> getDepartmentById(@PathVariable("id") int id){
        Department department = departmentService.getDepartmentById(id);
        DepartmentDto departmentResponse = modelMapper.map(department, DepartmentDto.class);
        return ResponseEntity.ok().body(departmentResponse);
   }
   @PostMapping("/create")
   public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto){
        Department departmentRequest = modelMapper.map(departmentDto, Department.class);
        Department department = departmentService.addDepartment(departmentRequest);
        DepartmentDto departmentResponse = modelMapper.map(department, DepartmentDto.class);
        return new ResponseEntity<DepartmentDto>(departmentResponse, HttpStatus.CREATED);

   }
   @PutMapping("/{id}")
  public ResponseEntity <DepartmentDto> updateDepartment(@PathVariable int id,@RequestBody DepartmentDto departmentDto){
        Department departmentRequest = modelMapper.map(departmentDto, Department.class);
        Department department = departmentService.updateDepartment(id,departmentRequest);
        DepartmentDto departmentResponse = modelMapper.map(department, DepartmentDto.class);
        return ResponseEntity.ok().body(departmentResponse);

   }
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteDepartment(@PathVariable("id") int id){
        departmentService.deleteDepartmentUsingId(id);
        return new ResponseEntity<String>("Department Deleted Successfully!", HttpStatus.OK);
   }


}
