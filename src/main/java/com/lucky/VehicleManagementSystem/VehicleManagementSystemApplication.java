package com.lucky.VehicleManagementSystem;

import com.lucky.VehicleManagementSystem.enums.EUserRole;
import com.lucky.VehicleManagementSystem.services.IRoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class VehicleManagementSystemApplication {

	private final IRoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(VehicleManagementSystemApplication.class, args);
	}

	@PostConstruct
	public void seedData() {
		Set<EUserRole> userRoleSet = new HashSet<>();
		userRoleSet.add(EUserRole.ADMIN);
		userRoleSet.add(EUserRole.STANDARD);
		for (EUserRole role : userRoleSet) {
			if (!this.roleService.isRolePresent(role)) {
				this.roleService.createRole(role);
			}
		}
	}
}
