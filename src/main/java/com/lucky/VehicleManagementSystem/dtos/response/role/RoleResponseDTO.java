package com.lucky.VehicleManagementSystem.dtos.response.role;


import com.lucky.VehicleManagementSystem.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RoleResponseDTO {
    private Role role;
}