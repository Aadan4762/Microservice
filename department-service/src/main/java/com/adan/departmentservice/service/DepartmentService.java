package com.adan.departmentservice.service;

import com.adan.departmentservice.dto.DepartmentRequest;
import com.adan.departmentservice.dto.DepartmentResponse;
import com.adan.departmentservice.model.Department;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse>getAllDepartment();
    DepartmentResponse getDepartmentById(int id);

    void addDepartment(DepartmentRequest departmentRequest);
    boolean updateDepartment(int id, DepartmentRequest departmentRequest);
    boolean deleteDepartmentById(int id);



}
