package com.lucky.VehicleManagementSystem.utils.helpers;


import com.lucky.VehicleManagementSystem.dtos.request.owner.CreateOwnerDto;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.Role;
import com.lucky.VehicleManagementSystem.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OwnerHelper {

    public User buildUserFromDto(CreateOwnerDto dto, Role role, PasswordEncoder passwordEncoder){
        return  User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .fullName(dto.getFirstName() + " " + dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nationalId(dto.getNationalId())
                .phoneNumber(dto.getPhoneNumber())
                .roles(Set.of(role))
                .build();
    }


    public Owner buildOwner(User user, CreateOwnerDto dto){
        return Owner.builder()
                .profile(user)
                .address(dto.getAddress())
                .build();
    }
}
