package com.adan.departmentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {

    @NotBlank(message = "Name shouldn't be null")
    private String name;
}
