package com.adan.departmentservice.service;

import com.adan.departmentservice.exception.ResourceNotFoundException;
import com.adan.departmentservice.model.Department;
import com.adan.departmentservice.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImplementation implements DepartmentService{

    private DepartmentRepository departmentRepository;

    public DepartmentServiceImplementation(DepartmentRepository departmentRepository) {
        super();
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAlldepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(int id) {
        Optional<Department> result = departmentRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }else {
            throw new ResourceNotFoundException("Department","id",id);
        }
    }

    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        Department departmental = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmental.setName(department.getName());
        return departmentRepository.save(departmental);
    }

    @Override
    public void deleteDepartmentUsingId(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id",id));
        departmentRepository.delete(department);


    }
}
