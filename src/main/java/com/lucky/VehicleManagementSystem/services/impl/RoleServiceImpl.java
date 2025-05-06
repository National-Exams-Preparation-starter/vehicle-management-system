package com.lucky.VehicleManagementSystem.services.impl;


import com.lucky.VehicleManagementSystem.dtos.request.role.CreateRoleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.role.RoleResponseDTO;
import com.lucky.VehicleManagementSystem.dtos.response.role.RolesResponseDTO;
import com.lucky.VehicleManagementSystem.enums.EUserRole;
import com.lucky.VehicleManagementSystem.exceptions.InternalServerErrorException;
import com.lucky.VehicleManagementSystem.exceptions.NotFoundException;
import com.lucky.VehicleManagementSystem.models.Role;
import com.lucky.VehicleManagementSystem.repository.IRoleRepository;
import com.lucky.VehicleManagementSystem.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public Role getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role with ID " + roleId + " not found"));
    }

    @Override
    public Role getRoleByName(EUserRole roleName) {
        return roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role " + roleName + " not found"));
    }

    @Override
    public void createRole(EUserRole roleName) {
        if (roleRepository.findRoleByName(roleName).isPresent()) {
            throw new InternalServerErrorException("Role " + roleName + " already exists");
        }

        Role role = Role.builder()
                .name(roleName)
                .build();

        try {
            roleRepository.save(role);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not create role: " + e.getMessage());
        }
    }

    @Override
    public RoleResponseDTO createRole(CreateRoleDTO createRoleDTO) {
        EUserRole roleName = EUserRole.valueOf(createRoleDTO.getName().name().toUpperCase());

        if (roleRepository.findRoleByName(roleName).isPresent()) {
            throw new InternalServerErrorException("Role " + roleName + " already exists");
        }

        Role role = Role.builder()
                .name(roleName)
                .build();

        try {
            Role savedRole = roleRepository.save(role);
            return RoleResponseDTO.builder()
                     .role(savedRole)
                    .build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not create role: " + e.getMessage());
        }
    }

    @Override
    public RolesResponseDTO getRoles(Pageable pageable) {
        try {
            Page<Role> rolePage = roleRepository.findAll(pageable);
            return RolesResponseDTO.builder()
                    .roles(rolePage)
                    .build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to fetch roles: " + e.getMessage());
        }
    }



    @Override
    public void deleteRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("The role is not present"));

        try {
            roleRepository.delete(role);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public boolean isRolePresent(EUserRole roleName) {
        try {
            return roleRepository.findRoleByName(roleName).isPresent();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
