package com.lucky.VehicleManagementSystem.controllers;

import com.lucky.VehicleManagementSystem.dtos.request.vehicle.CreateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.request.vehicle.UpdateVehicleDTO;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.PaginatedResponse;
import com.lucky.VehicleManagementSystem.dtos.response.vehicle.VehicleResponseDTO;
import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.services.IVehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("vehicle")
@RequiredArgsConstructor
@Tag(name = "Vehicles")
public class VehicleController {
    private final IVehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> createVehicle(@Valid @RequestBody CreateVehicleDTO dto){
        VehicleResponseDTO response = vehicleService.createVehicle(dto);
        return ApiResponse.success("Vehicle created successfully", HttpStatus.CREATED,response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleByVehicleId(@PathVariable UUID vehicleId){
        VehicleResponseDTO response = vehicleService.getVehicleById(vehicleId);
        return ApiResponse.success("Vehicle retrieved successfully", HttpStatus.OK,response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> searchVehicle(@RequestParam String keyword ){
        try{
            VehicleResponseDTO response = vehicleService.searchVehicle(keyword);
            return ApiResponse.success("Vehicle Get successfully", HttpStatus.OK, response);
        }catch (Exception e){
            return ApiResponse.fail("Failed to get vehicle", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicle(@PathVariable UUID id, @Valid @RequestBody UpdateVehicleDTO dto){
        try{
            VehicleResponseDTO response = vehicleService.updateVehicle(id, dto);
            return ApiResponse.success("Vehicle Update successfully", HttpStatus.OK, response);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to update vehicle", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Page<VehicleResponseDTO>>>> getAllVehicles(Pageable pageable){
        try{
            List<Page<VehicleResponseDTO>> response = vehicleService.getAllVehicles(pageable);
            return ApiResponse.success("Vehicles Retrieved Successfully", HttpStatus.OK , response);
        }catch (Exception e){
            return ApiResponse.fail("Failed to get vehicles", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<VehicleResponseDTO>>> getVehiclesByOwner(
            @PathVariable UUID ownerId,
            @PageableDefault(size = 10) Pageable pageable) {

        try{
            Page<VehicleResponseDTO> page = vehicleService.getVehiclesByOwnerId(ownerId, pageable);

            PaginatedResponse<VehicleResponseDTO> response = PaginatedResponse.<VehicleResponseDTO>builder()
                    .page(page.getNumber())
                    .size(page.getSize())
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .numberOfElements(page.getNumberOfElements())
                    .first(page.isFirst())
                    .last(page.isLast())
                    .data(page.getContent())
                    .build();

            return ApiResponse.success("Vehicles retrieved successfully", HttpStatus.OK, response);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieve vehicles", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable UUID id){
        try{
            vehicleService.deleteVehicle(id);
            return ApiResponse.success("Vehicle Deleted Successfully", HttpStatus.NO_CONTENT, null);
        }catch (Exception e){
            return ApiResponse.fail("Failed to delete vehicle", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
