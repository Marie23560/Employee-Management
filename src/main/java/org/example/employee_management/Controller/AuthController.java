package org.example.employee_management.Controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.employee_management.Dtos.UserDto.LoginRequest;
import org.example.employee_management.Dtos.UserDto.LoginResponse;
import org.example.employee_management.Dtos.UserDto.RegisterRequest;
import org.example.employee_management.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Data
@AllArgsConstructor
public class AuthController {


    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user and stores their details in the database")
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return userService.register(request);
    }

    @Operation(summary = "Login user", description = "Authenticates user and returns a JWT token")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        return userService.login(request);
    }
}
