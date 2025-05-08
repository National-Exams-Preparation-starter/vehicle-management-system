package com.lucky.VehicleManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends Base{
    @OneToOne
    @JoinColumn(name = "profile_id")
    private User profile;


    private String address;


    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<PlateNumber> plateNumbers;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<OwnershipRecord> ownershipRecords;
}
