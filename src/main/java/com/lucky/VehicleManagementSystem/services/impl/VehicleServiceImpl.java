package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.dtos.request.vehicle.CreateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.request.vehicle.UpdateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.VehicleResponseDTO;
import com.lucky.VehicleManagementSystem.enums.EPlateStatus;
import com.lucky.VehicleManagementSystem.exceptions.AppException;
import com.lucky.VehicleManagementSystem.exceptions.BadRequestException;
import com.lucky.VehicleManagementSystem.exceptions.NotFoundException;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.OwnershipRecord;
import com.lucky.VehicleManagementSystem.models.PlateNumber;
import com.lucky.VehicleManagementSystem.models.Vehicle;
import com.lucky.VehicleManagementSystem.repository.IOwnerRepository;
import com.lucky.VehicleManagementSystem.repository.IOwnerShipRecordRepository;
import com.lucky.VehicleManagementSystem.repository.IPlateNumberRepository;
import com.lucky.VehicleManagementSystem.repository.IVehicleRepository;
import com.lucky.VehicleManagementSystem.services.IOwnerService;
import com.lucky.VehicleManagementSystem.services.IVehicleService;
import com.lucky.VehicleManagementSystem.utils.Mapper;
import com.lucky.VehicleManagementSystem.utils.helpers.ChassisNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {
    private final IOwnerRepository ownerRepository;
    private final IVehicleRepository vehicleRepository;
    private final IOwnerShipRecordRepository ownerShipRecordRepository;
    private final IPlateNumberRepository plateNumberRepository;
    private final ChassisNumberGenerator chassisNumberGenerator;

    @Override
    public VehicleResponseDTO createVehicle(CreateVehicleDTO dto) {
        try {
            Owner owner = ownerRepository.findById(dto.getOwnerId()).orElseThrow(
                    ()->new NotFoundException("Owner with id " + dto.getOwnerId() + " not found")
            );
            PlateNumber plateNumber = plateNumberRepository.findById(dto.getPlateId()).orElseThrow(
                    ()->new NotFoundException("PlateNumber with id " + dto.getPlateId() + " not found")
            );

            if(!plateNumber.getPlateStatus().equals(EPlateStatus.AVAILABLE)) {
                throw new NotFoundException("Plate number is already in use");
            }

            if(!plateNumber.getOwner().getId().equals(owner.getId())){
                throw new BadRequestException("Owner id not match");
            }

            Vehicle vehicle = Mapper.getMapper().map(dto, Vehicle.class);
            String ChassisNumber = chassisNumberGenerator.generateUniqueChasisNumber();

            vehicle.setChassisNumber(ChassisNumber);
            vehicle.setCurrentPlate(plateNumber);
            vehicle.setOwner(owner);

//            saving the vehicle
            vehicleRepository.save(vehicle);

//            setting the plate in use
            plateNumber.setPlateStatus(EPlateStatus.IN_USE);
            plateNumberRepository.save(plateNumber);

            OwnershipRecord ownerShipRecord = OwnershipRecord.builder()
                    .vehicle(vehicle)
                    .owner(owner)
                    .plateNumber(plateNumber)
                    .purchasePrice(dto.getPrice())
                    .transferDate(LocalDateTime.now())
                    .build();

            ownerShipRecordRepository.save(ownerShipRecord);

            return Mapper.getMapper().map(vehicle, VehicleResponseDTO.class);

        } catch (Exception e) {
            throw new AppException("Error creating vehicle: " + e.getMessage());
        }
    }

    @Override
    public VehicleResponseDTO getVehicleById(UUID vehicleId) {
       return vehicleRepository.findById(vehicleId)
                .map(vehicle -> Mapper.getMapper().map(vehicle, VehicleResponseDTO.class))
                .orElseThrow(
                ()->new NotFoundException("Vehicle with id not found")
        );
    }

    @Override
    public VehicleResponseDTO searchVehicle(String searchQuery) {
        return vehicleRepository.findVehicleByChassisNumber(searchQuery)
                .or(()->vehicleRepository.findVehicleByOwner_Profile_NationalId(searchQuery))
                .or(()->vehicleRepository.findVehicleByCurrentPlate_PlateNumber(searchQuery))
                .map(vehicle -> Mapper.getMapper().map(vehicle, VehicleResponseDTO.class))
                .orElseThrow(
                        ()->new NotFoundException("Vehicle with chassis number or national id or plate number " + searchQuery + " not found")
                );
    }

    @Override
    public VehicleResponseDTO updateVehicle(UUID vehicleId, UpdateVehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                ()->new NotFoundException("Vehicle with id " + vehicleId + " not found")
        );
        try{
            Mapper.getMapper().map(dto, vehicle);
            vehicle = vehicleRepository.save(vehicle);
            return Mapper.getMapper().map(vehicle, VehicleResponseDTO.class);
        } catch (Exception e) {
            throw new AppException("Failed to update vehicle " + e.getMessage());
        }
    }

    @Override
    public List<Page<VehicleResponseDTO>> getAllVehicles(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        return List.of(vehicles.map(vehicle -> Mapper.getMapper().map(vehicle, VehicleResponseDTO.class)));
    }

    @Override
    public Page<VehicleResponseDTO> getVehiclesByOwnerId(UUID ownerId, Pageable pageable) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        Page<Vehicle> page = vehicleRepository.findByOwner(owner, pageable);

        return page.map(vehicle -> Mapper.getMapper().map(vehicle, VehicleResponseDTO.class));
    }

    @Override
    public void deleteVehicle(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        vehicleRepository.delete(vehicle);
    }
}
