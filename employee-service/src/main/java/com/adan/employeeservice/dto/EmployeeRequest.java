package com.adan.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequest {
    @NotBlank(message = "Name shouldn't be null")
    private String name;
    @Email
    @NotBlank(message = "Email shouldn't be null")
    private String email;
    @NotBlank(message = "Department shouldn't be null")
    private String department;
}
