package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.dtos.request.vehicle.TransferVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.VehicleOwnershipResponseDTO;
import com.lucky.VehicleManagementSystem.enums.EPlateStatus;
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
import com.lucky.VehicleManagementSystem.services.IOwnershipRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerShipRecordServiceImpl implements IOwnershipRecordService {
    private final IOwnerShipRecordRepository ownerShipRecordRepository;
    private final IOwnerRepository ownerRepository;
    private final IPlateNumberRepository plateNumberRepository;
    private final IVehicleRepository vehicleRepository;

    @Override
    public void transferVehicleOwnership(TransferVehicleDTO dto) {
        try {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElseThrow(
                    ()->new NotFoundException("Vehicle with id not found")
            );

            Owner owner = ownerRepository.findById(dto.getNewOwnerId()).orElseThrow(
                    ()->new NotFoundException("Owner with id not found")
            );

            PlateNumber newPlate = plateNumberRepository.findById(dto.getNewPlateId()).orElseThrow(
                    ()->new NotFoundException("PlateNumber with id not found")
            );

            if(!newPlate.getOwner().getId().equals(owner.getId())){
                throw new BadRequestException("Plate number is not owned by the new owner");
            }
            if(!newPlate.getPlateStatus().equals(EPlateStatus.AVAILABLE)){
                throw new BadRequestException("Plate number is already in use");
            }
            PlateNumber currentPlate = vehicle.getCurrentPlate();
            if(currentPlate !=null){
                currentPlate.setPlateStatus(EPlateStatus.AVAILABLE);
                plateNumberRepository.save(currentPlate);
            }

            vehicle.setCurrentPlate(newPlate);
            vehicle.setPrice(dto.getPurchasePrice());
            vehicle.setOwner(owner);
            vehicleRepository.save(vehicle);

            newPlate.setPlateStatus(EPlateStatus.IN_USE);
            plateNumberRepository.save(newPlate);

            OwnershipRecord ownershipRecord = OwnershipRecord.builder()
                    .plateNumber(newPlate)
                    .owner(owner)
                    .vehicle(vehicle)
                    .transferDate(LocalDateTime.now())
                    .purchasePrice(dto.getPurchasePrice())
                    .build();

            ownerShipRecordRepository.save(ownershipRecord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VehicleOwnershipResponseDTO> getOwnershipHistoryByChassis(String chassisNumber) {
        List<OwnershipRecord> ownershipRecords = ownerShipRecordRepository.findOwnershipRecordsByVehicle_ChassisNumber(chassisNumber);
        return ownershipRecords.stream()
                .sorted(Comparator.comparing(OwnershipRecord::getTransferDate).reversed())
                .map(ownershipRecord -> {
                    VehicleOwnershipResponseDTO dto = new VehicleOwnershipResponseDTO();
                    dto.setPlateNumber(ownershipRecord.getPlateNumber().getPlateNumber());
                    dto.setOwnerName(ownershipRecord.getOwner().getProfile().getFullName());
                    dto.setTransferDate(ownershipRecord.getTransferDate());
                    dto.setPurchasePrice(ownershipRecord.getPurchasePrice());
                    return dto;
                }).toList();
    }

    @Override
    public List<VehicleOwnershipResponseDTO> getOwnershipHistoryByPlate(String plateNumber) {
        List<OwnershipRecord> ownershipRecords = ownerShipRecordRepository.findOwnershipRecordsByPlateNumber_PlateNumber(plateNumber);
        return ownershipRecords.stream()
                .sorted(Comparator.comparing(OwnershipRecord::getTransferDate).reversed())
                .map(record -> {
                    VehicleOwnershipResponseDTO dto = new VehicleOwnershipResponseDTO();
                    dto.setOwnerName(record.getOwner().getProfile().getFirstName() + " " + record.getOwner().getProfile().getLastName());
                    dto.setPlateNumber(record.getPlateNumber().getPlateNumber());
                    dto.setPurchasePrice(record.getPurchasePrice());
                    dto.setTransferDate(record.getTransferDate());
                    return dto;
                })
                .toList();
    }
}
