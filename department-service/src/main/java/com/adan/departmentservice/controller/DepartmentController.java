package com.adan.departmentservice.controller;

import com.adan.departmentservice.dto.APIResponse;
import com.adan.departmentservice.dto.DepartmentRequest;
import com.adan.departmentservice.dto.DepartmentResponse;
import com.adan.departmentservice.model.Department;
import com.adan.departmentservice.service.DepartmentService;
import com.adan.departmentservice.service.DepartmentServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v2/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentServiceImplementation departmentServiceImplementation;

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
    public List<DepartmentResponse> getAllDepartment() {
        return departmentService.getAllDepartment();
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
    @GetMapping("/{field}")
    private APIResponse<List<Department>> getDepartmentsWithSort(@PathVariable String field) {
        List<Department> allDepartments = departmentServiceImplementation.findDepartmentsWithSorting(field);
        return new APIResponse<>(allDepartments.size(), allDepartments);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    private APIResponse<Page<Department>> getDepartmentsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Department> departmentsWithPagination = departmentServiceImplementation.findDepartmentsWithPagination(offset, pageSize);
        return new APIResponse<>(departmentsWithPagination.getSize(), departmentsWithPagination);
    }

    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
    private APIResponse<Page<Department>> getDepartmentsWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        Page<Department> departmentsWithPagination = departmentServiceImplementation.findDepartmentsWithPaginationAndSorting(offset, pageSize, field);
        return new APIResponse<>(departmentsWithPagination.getSize(), departmentsWithPagination);
    }

}
