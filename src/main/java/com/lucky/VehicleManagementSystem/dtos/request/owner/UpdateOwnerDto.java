package com.lucky.VehicleManagementSystem.dtos.request.owner;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOwnerDto {
    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;
    private String address;

    private String phoneNumber;
}
