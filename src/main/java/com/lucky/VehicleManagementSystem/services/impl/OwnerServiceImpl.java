package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.dtos.request.owner.CreateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.request.owner.UpdateOwnerDto;
import com.lucky.VehicleManagementSystem.dtos.response.owner.OwnerResponseDTO;
import com.lucky.VehicleManagementSystem.enums.EUserRole;
import com.lucky.VehicleManagementSystem.exceptions.AppException;
import com.lucky.VehicleManagementSystem.exceptions.BadRequestException;
import com.lucky.VehicleManagementSystem.exceptions.NotFoundException;
import com.lucky.VehicleManagementSystem.mapper.OwnerMapper;
import com.lucky.VehicleManagementSystem.models.Owner;
import com.lucky.VehicleManagementSystem.models.PlateNumber;
import com.lucky.VehicleManagementSystem.models.Role;
import com.lucky.VehicleManagementSystem.models.User;
import com.lucky.VehicleManagementSystem.repository.IOwnerRepository;
import com.lucky.VehicleManagementSystem.repository.IUserRepository;
import com.lucky.VehicleManagementSystem.services.IOwnerService;
import com.lucky.VehicleManagementSystem.services.IRoleService;
import com.lucky.VehicleManagementSystem.utils.helpers.OwnerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements IOwnerService {
    private final IOwnerRepository ownerRepository;
    private final IRoleService roleService;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerHelper ownerHelper;

    @Override
    public OwnerResponseDTO createOwner(CreateOwnerDto owner) {
        if(ownerRepository.existsOwnerByProfile_Email(owner.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        try{
            Role role = roleService.getRoleByName(EUserRole.STANDARD);

            User user = ownerHelper.buildUserFromDto(owner,role,passwordEncoder);
            user = userRepository.save(user);

            Owner newOwner = ownerHelper.buildOwner(user,owner);
            newOwner = ownerRepository.save(newOwner);

            return OwnerMapper.mapToOwnerResponseDTO(user,newOwner);
        } catch (Exception e) {
            throw new AppException("Failed to create owner: " + e.getMessage());
        }
    }

    @Override
    public Owner getOwnerById(UUID ownerId) {
        return ownerRepository.findById(ownerId).orElseThrow(
                ()->new NotFoundException("Owner with id " + ownerId + " not found")
        );
    }

    @Override
    public List<String> getPlateNumbersByOwnerId(UUID ownerId) {
        Owner owner = getOwnerById(ownerId);

        return owner
                .getPlateNumbers()
                .stream()
                .map(PlateNumber::getPlateNumber)
                .toList();
    }

    @Override
    public OwnerResponseDTO searchOwner(String searchQuery) {
        return ownerRepository.findOwnerByProfile_Email(searchQuery)
                .or(()->ownerRepository.findOwnerByProfile_PhoneNumber(searchQuery))
                .or(()->ownerRepository.findOwnerByProfile_NationalId(searchQuery))
                .map(owner->OwnerMapper.mapToOwnerResponseDTO(owner.getProfile(),owner))
                .orElseThrow(()->new NotFoundException("Owner with email or phone number or national id " + searchQuery + " not found"));
    }

    @Override
    public Page<OwnerResponseDTO> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(owner -> OwnerMapper.mapToOwnerResponseDTO(owner.getProfile(),owner));
    }

    @Override
    public Owner updateOwner(UUID ownerId, UpdateOwnerDto owner) {
        Owner ownerToUpdate = getOwnerById(ownerId);
        User profile = ownerToUpdate.getProfile();

        if (owner.getFirstName() != null){
            profile.setFirstName(owner.getFirstName());
        }
        if (owner.getLastName() != null){
            profile.setLastName(owner.getLastName());
        }

        if (owner.getFirstName() != null || owner.getLastName() != null) {
            String fullName = String.format("%s %s",
                    owner.getFirstName() != null ? owner.getFirstName() : profile.getFirstName(),
                    owner.getLastName() != null ? owner.getLastName() : profile.getLastName()
            );
            profile.setFullName(fullName.trim());
        }

        if(owner.getAddress() != null){
            ownerToUpdate.setAddress(owner.getAddress());
        }

        userRepository.save(profile);
        return ownerRepository.save(ownerToUpdate);
    }

    @Override
    public void deleteOwner(UUID ownerId) {
        Owner owner = getOwnerById(ownerId);
        User profile = owner.getProfile();

        userRepository.delete(profile);
        ownerRepository.delete(owner);

    }
}
