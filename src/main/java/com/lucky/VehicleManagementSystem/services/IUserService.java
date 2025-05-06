package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.auth.UpdateUserDTO;
import com.lucky.VehicleManagementSystem.dtos.request.user.CreateAdminDTO;
import com.lucky.VehicleManagementSystem.dtos.request.user.UserResponseDTO;
import com.lucky.VehicleManagementSystem.dtos.request.user.UserRoleModificationDTO;
import com.lucky.VehicleManagementSystem.models.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    User findUserById(UUID userId);

    User getLoggedInUser();

    UserResponseDTO createAdmin(CreateAdminDTO createUserDTO);

    List<User> getUsers();

    UserResponseDTO getUserById(UUID uuid);

    UserResponseDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO);

    UserResponseDTO addRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    UserResponseDTO removeRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    void deleteUser(UUID userId);
}
