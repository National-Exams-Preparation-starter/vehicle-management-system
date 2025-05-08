package com.lucky.VehicleManagementSystem.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucky.VehicleManagementSystem.enums.EUserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends Base {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private EUserRole name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;
}

