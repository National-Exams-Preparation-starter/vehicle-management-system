package com.lucky.VehicleManagementSystem.mapper;

import com.lucky.VehicleManagementSystem.dtos.response.owner.OwnerResponseDTO;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.User;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {


    public static OwnerResponseDTO mapToOwnerResponseDTO(User user , Owner owner){

        return OwnerResponseDTO.builder()
                .id(owner.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .nationalId(user.getNationalId())
                .Address(owner.getAddress())
                .build();
    }


}
