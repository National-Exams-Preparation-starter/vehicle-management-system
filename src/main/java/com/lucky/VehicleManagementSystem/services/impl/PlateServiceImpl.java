package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.enums.EPlateStatus;
import com.lucky.VehicleManagementSystem.exceptions.AppException;
import com.lucky.VehicleManagementSystem.exceptions.NotFoundException;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.PlateNumber;
import com.lucky.VehicleManagementSystem.repository.IOwnerRepository;
import com.lucky.VehicleManagementSystem.repository.IPlateNumberRepository;
import com.lucky.VehicleManagementSystem.services.IPlateService;
import com.lucky.VehicleManagementSystem.utils.helpers.PlateNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlateServiceImpl implements IPlateService {
    private final IPlateNumberRepository plateNumberRepository;
    private final IOwnerRepository ownerRepository;
    private final PlateNumberGenerator plateNumberGenerator;

    @Override
    public PlateNumber getPlateNumberByOwnerId(UUID ownerId) {
        try {
            Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                    ()->new NotFoundException("Owner with id " + ownerId + " not found")
            );

            String plateValue = plateNumberGenerator.generateNextPlateNumber();

            PlateNumber plateNumber = PlateNumber.builder()
                    .plateStatus(EPlateStatus.AVAILABLE)
                    .plateNumber(plateValue)
                    .issuedDate(LocalDateTime.now())
                    .owner(owner)
                    .build();

            return plateNumberRepository.save(plateNumber);

        } catch (Exception e) {
            throw new AppException("failed to create plate number: " + e.getMessage());
        }
    }
}
