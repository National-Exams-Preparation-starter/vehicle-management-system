package com.lucky.VehicleManagementSystem.repository;

import com.lucky.VehicleManagementSystem.models.OwnershipRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IOwnerShipRecordRepository extends JpaRepository<OwnershipRecord, UUID> {
    List<OwnershipRecord> findOwnershipRecordsByVehicle_ChassisNumber(String vehicleChassisNumber);

    List<OwnershipRecord> findOwnershipRecordsByPlateNumber_PlateNumber(String plateNumberPlateNumber);
}
