package com.lucky.VehicleManagementSystem.dtos.request.owner;

import com.lucky.VehicleManagementSystem.dtos.request.auth.RegisterDto;
import lombok.Data;

@Data
public class CreateOwnerDto extends RegisterDto {
    public String address;
}
