package com.lucky.VehicleManagementSystem.controllers;

import com.lucky.VehicleManagementSystem.models.PlateNumber;
import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.services.IPlateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("plate-number")
@RequiredArgsConstructor
@Tag(name = "Plate number", description = "Plate number management endpoints")
public class PlateNumberController {
    private final IPlateService plateService;

    @GetMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<PlateNumber>> getPlateNumberByOwnerId(@PathVariable UUID ownerId){
        PlateNumber response = plateService.getPlateNumberByOwnerId(ownerId);
        return ApiResponse.success("Plate number retrieved successfully", HttpStatus.OK,response);
    }
}
