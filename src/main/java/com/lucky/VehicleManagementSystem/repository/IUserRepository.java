package com.lucky.VehicleManagementSystem.repository;

import com.lucky.VehicleManagementSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
