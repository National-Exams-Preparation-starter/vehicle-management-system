package com.lucky.VehicleManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucky.VehicleManagementSystem.enums.EPlateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plate_numbers")
public class PlateNumber extends Base {

    @Column(unique = true)
    private String plateNumber;

    private LocalDateTime issuedDate;

    @Enumerated(EnumType.STRING)
    private EPlateStatus plateStatus;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;


    @OneToOne(mappedBy = "currentPlate")
    @JsonIgnore
    private Vehicle vehicle;

    @OneToMany(mappedBy = "plateNumber", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OwnershipRecord> ownershipRecords;

}
