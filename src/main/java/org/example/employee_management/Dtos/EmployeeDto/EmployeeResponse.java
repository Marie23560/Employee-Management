package org.example.employee_management.Dtos.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String position;
    private String department;
    private LocalDate hireDate;
    private Long managerId;

}
