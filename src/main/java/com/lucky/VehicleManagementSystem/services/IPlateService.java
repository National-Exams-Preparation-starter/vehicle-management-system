package com.lucky.VehicleManagementSystem.services;

import com.lucky.VehicleManagementSystem.models.PlateNumber;

import java.util.UUID;

public interface IPlateService {
    PlateNumber getPlateNumberByOwnerId(UUID ownerId);
}
