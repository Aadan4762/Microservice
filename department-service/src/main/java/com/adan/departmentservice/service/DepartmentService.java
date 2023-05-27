package com.adan.departmentservice.service;

import com.adan.departmentservice.model.Department;

import java.util.List;

public interface DepartmentService {

    List<Department>getAlldepartment();
    Department getDepartmentById(int id);

    Department addDepartment(Department department);
    Department updateDepartment(int id, Department department);
    void deleteDepartmentUsingId(int id);
}
