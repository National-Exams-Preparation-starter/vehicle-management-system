package com.lucky.VehicleManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucky.VehicleManagementSystem.enums.EAccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends Person {

    @Column(unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = true)
    private String password;

    @Transient
    private String fullName;

    @Enumerated(EnumType.STRING)
    private EAccountStatus accountStatus;

    private boolean isVerified = false;

    @JsonIgnore
    private String verificationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    private User(String email , String password){
        this.email = email;
        this.password = password;
    }
}
