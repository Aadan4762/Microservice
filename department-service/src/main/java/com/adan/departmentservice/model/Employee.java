package com.adan.departmentservice.model;

public record Employee(Long id,
                       Long department,
                       String name,
                       int age,
                       String position) {
}
