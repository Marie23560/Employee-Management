package org.example.employee_management.Security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Employee_management",
                description = "API for managing employees, users and authentication.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Marie Pascale",
                        email = "Kanzayiremarie@gmail.com",
                        url = "https://github.com/Marie23560/Employee-management"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {

}
