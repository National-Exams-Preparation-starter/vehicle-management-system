package com.lucky.VehicleManagementSystem.repository;

import com.lucky.VehicleManagementSystem.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IOwnerRepository extends JpaRepository<Owner, UUID> {
    Boolean existsOwnerByProfile_Email(String email);
    Optional<Owner> findOwnerByProfile_Email(String email);
    Optional<Owner> findOwnerByProfile_NationalId(String nationalId);
    Optional<Owner> findOwnerByProfile_PhoneNumber(String phoneNumber);
}
