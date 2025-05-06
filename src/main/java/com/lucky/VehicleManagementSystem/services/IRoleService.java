package com.lucky.VehicleManagementSystem.services;


import com.lucky.VehicleManagementSystem.dtos.request.role.CreateRoleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.role.RoleResponseDTO;
import com.lucky.VehicleManagementSystem.dtos.response.role.RolesResponseDTO;
import com.lucky.VehicleManagementSystem.enums.EUserRole;
import com.lucky.VehicleManagementSystem.models.Role;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRoleService {
    public Role getRoleById(UUID roleId);

    public Role getRoleByName(EUserRole roleName);

    public void createRole(EUserRole roleName);

    public RoleResponseDTO createRole(CreateRoleDTO createRoleDTO);

    public RolesResponseDTO getRoles(Pageable pageable);

    public void deleteRole(UUID roleId);

    public boolean isRolePresent(EUserRole roleName);
}