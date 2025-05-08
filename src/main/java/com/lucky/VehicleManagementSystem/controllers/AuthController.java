package com.lucky.VehicleManagementSystem.controllers;

import com.lucky.VehicleManagementSystem.dtos.request.auth.LoginDto;
import com.lucky.VehicleManagementSystem.dtos.request.auth.PasswordUpdateDTO;
import com.lucky.VehicleManagementSystem.dtos.request.auth.ResetPasswordDto;
import com.lucky.VehicleManagementSystem.dtos.request.user.CreateAdminDTO;
import com.lucky.VehicleManagementSystem.dtos.request.user.UserResponseDTO;
import com.lucky.VehicleManagementSystem.dtos.response.auth.AuthResponse;
import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.services.IAuthService;
import com.lucky.VehicleManagementSystem.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {

    private final IUserService userService;
    private final IAuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "User login", description = "Authenticates a user and returns access token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            logger.debug("Processing login request for user: {}", loginDto.getEmail());
            AuthResponse response = authService.login(loginDto);
            return ApiResponse.success("Logged in successfully", HttpStatus.OK,response);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", loginDto.getEmail(), e);
            return ApiResponse.fail("Login failed", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

//    registering the admin with admin code
    @PostMapping("/admin/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerAdmin(@Valid @RequestBody CreateAdminDTO createAdminDto){
        try {
            logger.debug("Creating admin account for: {}", createAdminDto.getEmail());
            UserResponseDTO response = userService.createAdmin(createAdminDto);
            return ApiResponse.success("Admin account created successfully", HttpStatus.OK,response);
        } catch (Exception e) {
            logger.error("Admin account creation failed for: {}", createAdminDto.getEmail(), e);
            return ApiResponse.fail("Admin account creation failed", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

//    initiating resent password process
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@RequestParam @Email String email){
        authService.forgotPassword(email);
        return ApiResponse.success("Reset Password Instructions sent to your email",HttpStatus.OK,null);
    }

//    reseting password
    @PostMapping("/reset-password")
    public  ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto){
        authService.resetPassword(resetPasswordDto);
        return ApiResponse.success("Password reset successfully",HttpStatus.OK,null);
    }

//    initiating account verification process
    @PostMapping("/initiate-verification")
    public ResponseEntity<ApiResponse<Object>> initiateVerification(@RequestParam @Email String email){
        authService.initiateAccountVerification(email);
        return ApiResponse.success("Account Verification initiated, check your email",HttpStatus.OK, null);
    }

//    verifying the account of the user
    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Object>> verifyAccount(@RequestParam String verificationCode){
        authService.verifyAccount(verificationCode);
        return ApiResponse.success("Account Verified Successfully",HttpStatus.OK,null);
    }

//    resend account verification code
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Object>> resendVerificationCode(@RequestParam @Email String email){
        authService.resendVerificationCode(email);
        return ApiResponse.success("Verification code resent Successful",HttpStatus.OK,null);
    }
//    updating the password
    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse<Object>> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto){
        String email= userService.getLoggedInUser().getEmail();
        authService.updatePassword(email,dto.getOldPassword(),dto.getNewPassword());
        return ApiResponse.success("Password updated successfully",HttpStatus.OK,null);
    }

}
