package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.vehicle.CreateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.request.vehicle.UpdateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.VehicleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IVehicleService {
    VehicleResponseDTO createVehicle(CreateVehicleDTO dto);

    VehicleResponseDTO getVehicleById(UUID vehicleId);

    VehicleResponseDTO searchVehicle(String searchQuery);

    VehicleResponseDTO updateVehicle(UUID vehicleId, UpdateVehicleDTO dto);

    List<Page<VehicleResponseDTO>> getAllVehicles(Pageable pageable);

    Page<VehicleResponseDTO> getVehiclesByOwnerId(UUID ownerId, Pageable pageable);

    void deleteVehicle(UUID vehicleId);
}
