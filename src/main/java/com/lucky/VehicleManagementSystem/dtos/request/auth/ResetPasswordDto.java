package com.lucky.VehicleManagementSystem.dtos.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    public String email;
    public String passwordResetCode;
    public String newPassword;
}
