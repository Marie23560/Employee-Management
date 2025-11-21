package org.example.employee_management.Dtos.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for user login")
public class LoginRequest {

    @NotBlank(message = "Email is Requested")
    @Email(message = "valid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
