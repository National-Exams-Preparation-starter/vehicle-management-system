package com.lucky.VehicleManagementSystem.repository;

import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IVehicleRepository extends JpaRepository<Vehicle, UUID> {
    Page<Vehicle> findByOwner(Owner owner, Pageable pageable);

    boolean existsByChassisNumber(String chassisNumber);

    Optional<Vehicle> findVehicleByOwner_Profile_NationalId(String nationalId);

    Optional<Vehicle> findVehicleByCurrentPlate_PlateNumber(String plateNumber);

    Optional<Vehicle> findVehicleByChassisNumber(String chassisNumber);

}
