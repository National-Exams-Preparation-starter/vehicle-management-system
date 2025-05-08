package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.owner.CreateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.request.owner.UpdateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.response.owner.OwnerResponseDTO;
import com.lucky.VehicleManagementSystem.models.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IOwnerService {
    OwnerResponseDTO createOwner(CreateOwnerDto owner);

    Owner getOwnerById(UUID ownerId);

    List<String> getPlateNumbersByOwnerId(UUID ownerId);

    OwnerResponseDTO searchOwner(String searchQuery);

    Page<OwnerResponseDTO> getAllOwners(Pageable pageable);

    Owner updateOwner(UUID ownerId, UpdateOwnerDto owner);

    void deleteOwner(UUID ownerId);
}
