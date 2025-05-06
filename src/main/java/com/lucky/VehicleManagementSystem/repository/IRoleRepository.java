package com.lucky.VehicleManagementSystem.repository;

import com.lucky.VehicleManagementSystem.enums.EUserRole;
import com.lucky.VehicleManagementSystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByName(EUserRole name);

    boolean existsByName(String name);
}
