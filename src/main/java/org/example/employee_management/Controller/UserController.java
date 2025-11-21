package org.example.employee_management.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.employee_management.Dtos.UserDto.UserRequestDTO;
import org.example.employee_management.Dtos.UserDto.UserResponseDTO;
import org.example.employee_management.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "This endpoint shows a single user, the corresponding email and the role." +
                    "P.S.: An ADMIN can access all kinds of ids but a USER can only access their own id alone.",
            summary = "Get A User By Id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409"
                    ),
            }
    )
    @GetMapping("/user/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id, HttpServletRequest request){
        return userService.getUserById(id, request);
    }

    @Operation(
            description = "This endpoint allows to patch a User's credentials or for the admin to assign roles to other user's except themself. The rest of the data is conserved as is in the DB.",
            summary = "Patches A User's credentials",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409"
                    ),
            }
    )
    @PatchMapping("/user/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request){
        return userService.updateUser(id, userRequestDTO, request);
    }

    @Operation(
            description = "This endpoint allows ADMIN role to view all users. This method also renders everything in form of pages and applies some sorting as per the user requirements.",
            summary = "Get All users with their associated employees",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
            }
    )
    @GetMapping("/user/all")
    public Page<UserResponseDTO> getAllUsers(HttpServletRequest request,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "username") String sortBy,
                                             @RequestParam(defaultValue = "desc") String direction){
        return userService.getAllUsers(request, page, size, sortBy, direction);
    }

    @Operation(
            description = "This endpoint allows a USER to delete one's account or the ADMIN to delete any account of choice.",
            summary = "Deletes A user's Credentials",
            responses = {
                    @ApiResponse(
                            description = "Success/No Content",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
            }
    )
    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request){
        return userService.delete(id,request);
    }

}
