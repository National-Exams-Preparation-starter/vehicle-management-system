package com.lucky.VehicleManagementSystem.controllers;

import com.lucky.VehicleManagementSystem.dtos.request.auth.UpdateUserDTO;
import com.lucky.VehicleManagementSystem.dtos.request.user.UserResponseDTO;
import com.lucky.VehicleManagementSystem.models.User;
import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.services.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User management", description = "System user management")
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(){
        User response  = userService.getLoggedInUser();

        return ApiResponse.success(null, HttpStatus.OK,response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(){
        List<User> users = userService.getUsers();
        List<UserResponseDTO> userResponseDtos = users.stream().map(UserResponseDTO::new).toList();
        return ApiResponse.success(null, HttpStatus.OK,userResponseDtos);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable UUID userId){
        UserResponseDTO userResponseDto = userService.getUserById(userId);
        return ApiResponse.success("user retrieved successfully", HttpStatus.OK,userResponseDto);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserDTO updateUserDTO
    ){
        UserResponseDTO updatedUser = userService.updateUser(userId,updateUserDTO);
        return ApiResponse.success("user updated successfully", HttpStatus.OK,updatedUser);
    }


}
