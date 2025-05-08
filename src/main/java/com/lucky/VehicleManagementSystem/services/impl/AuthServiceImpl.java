package com.lucky.VehicleManagementSystem.services.impl;

import com.lucky.VehicleManagementSystem.dtos.request.auth.LoginDto;
import com.lucky.VehicleManagementSystem.dtos.request.auth.ResetPasswordDto;
import com.lucky.VehicleManagementSystem.dtos.response.auth.AuthResponse;
import com.lucky.VehicleManagementSystem.enums.IEmailTemplate;
import com.lucky.VehicleManagementSystem.exceptions.AppException;
import com.lucky.VehicleManagementSystem.exceptions.BadRequestException;
import com.lucky.VehicleManagementSystem.models.User;
import com.lucky.VehicleManagementSystem.repository.IUserRepository;
import com.lucky.VehicleManagementSystem.security.jwt.JWTUtils;
import com.lucky.VehicleManagementSystem.security.user.UserPrincipal;
import com.lucky.VehicleManagementSystem.services.IAuthService;
import com.lucky.VehicleManagementSystem.services.IUserService;
import com.lucky.VehicleManagementSystem.standalone.EmailService;
import com.lucky.VehicleManagementSystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;
    private final EmailService emailService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;


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

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                ()->new BadRequestException(String.format("User with email %s not found", email) )
        );
        String resetCode = UserUtils.generateToken();
        user.setPasswordResetCode(resetCode);
        user.setPasswordResetCodeGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

//        setting email things
        Map<String, Object> variables = new HashMap<>();
        variables.put("code",resetCode);

        try{
            emailService.sendEmail(email,
                    user.getFirstName(),
                    "Reset Your Password",
                    IEmailTemplate.RESET_PASSWORD,
                    variables
            );
        } catch (Exception e) {
            throw new AppException("Failed to send email: "+e.getMessage());
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findUserByEmail(resetPasswordDto.getEmail()).orElseThrow(
                ()->new BadRequestException(String.format("User with email %s not found", resetPasswordDto.getEmail()) )
        );
        if(!resetPasswordDto.passwordResetCode.equals(user.getPasswordResetCode())){
            throw new BadRequestException("Password reset code not match");
        }
        if(user.getPasswordResetCodeGeneratedAt() == null ||
                user.getPasswordResetCodeGeneratedAt().isBefore(LocalDateTime.now().minusMinutes(15))){
            throw new BadRequestException("Password reset code expired");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setPasswordResetCode(null);
        user.setPasswordResetCodeGeneratedAt(null);
        userRepository.save(user);
    }

    @Override
    public void initiateAccountVerification(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                ()->new BadRequestException(String.format("User with email %s not found",email))
        );
        if(user.isVerified()) return;
//        genetating the token
        String verificationCode = UserUtils.generateToken();

        user.setVerificationCode(verificationCode);
        user.setVerificationCodeCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("code",verificationCode);

        try {
            emailService.sendEmail(email,
                    user.getFirstName(),
                    "Account Verification Initiated",
                    IEmailTemplate.ACCOUNT_VERIFICATION,
                    variables
            );
        } catch (Exception e) {
            throw new AppException("Failed to send email: "+e.getMessage());
        }

    }

    @Override
    public void verifyAccount(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow(
                ()->new BadRequestException("Invalid verification code")
        );
        if(user.isVerified()) {
            throw new BadRequestException("Account already verified");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeCreatedAt(null);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                ()->new BadRequestException(String.format("User with email %s not found",email))
        );
        if(user.isVerified()) {
            throw new BadRequestException("Account already verified");
        }
        String verificationCode = UserUtils.generateToken();

        user.setVerificationCode(verificationCode);
        user.setVerificationCodeCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();
        variables.put("code",verificationCode);

        try {
            emailService.sendEmail(email,
                    user.getFirstName(),
                    "Account Verification",
                    IEmailTemplate.ACCOUNT_VERIFICATION,
                    variables
            );
        } catch (Exception e) {
            throw new AppException("Failed to send email: "+e.getMessage());
        }
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                ()->new BadRequestException(String.format("User with email %s not found",email))
        );
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
