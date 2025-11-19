package org.example.employee_management.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response body for employee-related operations")
public class EmployeeResponse {
    private Long id;
    private String name;
    private String position;
    private String department;
    private LocalDate hireDate;
    private Long managerId;

}
