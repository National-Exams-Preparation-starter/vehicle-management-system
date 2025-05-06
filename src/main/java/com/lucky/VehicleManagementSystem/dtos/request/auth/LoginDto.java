package com.lucky.VehicleManagementSystem.dtos.request.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String email;
    private String password;
}
