package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.dtos.request.vehicle.TransferVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.VehicleOwnershipResponseDTO;

import java.util.List;

public interface IOwnershipRecordService {

    void transferVehicleOwnership(TransferVehicleDTO dto);
    List<VehicleOwnershipResponseDTO> getOwnershipHistoryByChassis(String chassisNumber);
    List<VehicleOwnershipResponseDTO> getOwnershipHistoryByPlate(String plateNumber);


}
