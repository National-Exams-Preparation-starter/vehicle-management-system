package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.auth.LoginDto;
import com.lucky.VehicleManagementSystem.dtos.request.auth.ResetPasswordDto;
import com.lucky.VehicleManagementSystem.dtos.response.auth.AuthResponse;

public interface IAuthService {
    AuthResponse login(LoginDto signInDto);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    void initiateAccountVerification(String email);

    void verifyAccount(String VerificationCode);

    void resendVerificationCode(String email);

    void updatePassword(String email,String oldPassword, String newPassword);
}
