package com.lucky.VehicleManagementSystem.dtos.request.role;


import com.lucky.VehicleManagementSystem.enums.EUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRoleDTO {
    @Schema(example = "ADMIN", description = "Role name")
    private EUserRole name;
}
