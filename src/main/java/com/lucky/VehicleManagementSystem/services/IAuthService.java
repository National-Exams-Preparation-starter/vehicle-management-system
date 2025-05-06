package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.auth.LoginDto;
import com.lucky.VehicleManagementSystem.dtos.response.auth.AuthResponse;

public interface IAuthService {
    AuthResponse login(LoginDto signInDto);

}
