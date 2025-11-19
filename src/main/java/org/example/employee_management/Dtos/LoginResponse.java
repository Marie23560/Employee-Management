package org.example.employee_management.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response returned after successful login")
public class LoginResponse {

    @Schema(description = "JWT token for authentication")
    private String token;
    private Long id;
}
