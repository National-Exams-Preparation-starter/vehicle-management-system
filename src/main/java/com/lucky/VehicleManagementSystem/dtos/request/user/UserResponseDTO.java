package com.lucky.VehicleManagementSystem.dtos.request.user;


import com.lucky.VehicleManagementSystem.models.User;
import com.mikepn.vehiclemanagementsystem.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private User user;
}