package com.lucky.VehicleManagementSystem.dtos.request.auth;


import com.lucky.VehicleManagementSystem.annotations.ValidPassword;
import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private String oldPassword;
    @ValidPassword(message = "Password should be strong")
    private String newPassword;
}