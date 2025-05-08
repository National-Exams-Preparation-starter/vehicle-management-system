package com.lucky.VehicleManagementSystem.controllers;

import com.lucky.VehicleManagementSystem.dtos.request.owner.CreateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.request.owner.UpdateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.response.owner.OwnerResponseDTO;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.services.IOwnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("owners")
@RequiredArgsConstructor
@Tag(name="Owner")
public class OwnerController {
    private final IOwnerService ownerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OwnerResponseDTO>> createOwner(@Valid @RequestBody CreateOwnerDto dto){
       OwnerResponseDTO response = ownerService.createOwner(dto);
       return ApiResponse.success("Owner created successfully", HttpStatus.CREATED,response);
    }

    @GetMapping("/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Owner>> getOwnerByOwnerId(@PathVariable UUID ownerId){
        Owner response = ownerService.getOwnerById(ownerId);
        return ApiResponse.success("Owner retrieved successfully", HttpStatus.OK,response);
    }

    @GetMapping("/plate-numbers/{ownerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STANDARD')")
    public ResponseEntity<ApiResponse<List<String>>> getPlateNumbersByOwnerId(@PathVariable UUID ownerId){
        List<String> response = ownerService.getPlateNumbersByOwnerId(ownerId);
        return ApiResponse.success("Plate numbers retrieved successfully", HttpStatus.OK,response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OwnerResponseDTO>> searchOwner(@RequestParam String searchQuery){
        OwnerResponseDTO response = ownerService.searchOwner(searchQuery);
        return ApiResponse.success("Owner retrieved successfully", HttpStatus.OK,response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<OwnerResponseDTO>>> getAllOwners(Pageable pageable){
       Page<OwnerResponseDTO> owners = ownerService.getAllOwners(pageable);
       return ApiResponse.success("Owners retrieved successfully", HttpStatus.OK,owners);
    }

    @PutMapping("/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Owner>> updateOwner(
            @PathVariable UUID ownerId,
            @Valid @RequestBody UpdateOwnerDto dto
    )
    {
        Owner owner = ownerService.updateOwner(ownerId,dto);
        return ApiResponse.success("Owner updated successfully", HttpStatus.OK, owner);
    }

    @DeleteMapping("/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteOwner(@PathVariable UUID ownerId){
        ownerService.deleteOwner(ownerId);
        return ApiResponse.success("Owner deleted successfully", HttpStatus.OK,null);
    }

}
