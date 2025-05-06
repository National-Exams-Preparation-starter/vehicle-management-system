package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.dtos.request.auth.LoginDto;
import com.lucky.VehicleManagementSystem.dtos.response.auth.AuthResponse;
import com.lucky.VehicleManagementSystem.models.User;
import com.lucky.VehicleManagementSystem.repository.IUserRepository;
import com.lucky.VehicleManagementSystem.security.jwt.JWTUtils;
import com.lucky.VehicleManagementSystem.security.user.UserPrincipal;
import com.lucky.VehicleManagementSystem.services.IAuthService;
import com.lucky.VehicleManagementSystem.services.IUserService;
import com.lucky.VehicleManagementSystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;
    private final IUserService userService;


    private Authentication authenticateUser(LoginDto loginDTO) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private AuthResponse generateJwtAuthenticationResponse(Authentication authentication) {
        String jwt = jwtUtils.generateToken(authentication);
        UserPrincipal userPrincipal = UserUtils.getLoggedInUser();
        assert userPrincipal != null;
        User user = userService.findUserById(userPrincipal.getId());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        return new AuthResponse(jwt, user);
    }

    @Override
    public AuthResponse login(LoginDto signInDto) {
        Authentication authentication = authenticateUser(signInDto);
        return generateJwtAuthenticationResponse(authentication);
    }
}
