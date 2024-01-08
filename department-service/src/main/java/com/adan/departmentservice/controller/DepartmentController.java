package com.adan.departmentservice.controller;

import com.adan.departmentservice.dto.DepartmentRequest;
import com.adan.departmentservice.dto.DepartmentResponse;
import com.adan.departmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v2/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createDepartment(@RequestBody @Valid DepartmentRequest departmentRequest) {
        try {
            departmentService.addDepartment(departmentRequest);
            return "Department created successfully";
        } catch (Exception exception) {
            return "Failed to create department" + exception.getMessage();
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentResponse> getAllDepartments(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return departmentService.getAllDepartment(sortBy, sortOrder, page, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getDepartmentById(@PathVariable int id) {
        DepartmentResponse department = departmentService.getDepartmentById(id);

        if (department != null) {
            return department;
        }else {
            return "Department not found";
        }

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateDepartmentById(@PathVariable int id, @RequestBody DepartmentRequest departmentRequest){
        boolean isUpdated = departmentService.updateDepartment(id,departmentRequest);
        if (isUpdated){
            return "Department updated successfully";
        }else {
            return "Department did not update";
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteDepartmentById(@PathVariable int id){
        boolean isDeleted = departmentService.deleteDepartmentById(id);

        if (isDeleted){
            return "Department deleted successfully";
        }else {
            return "Department could not be found";
        }
    }

}
