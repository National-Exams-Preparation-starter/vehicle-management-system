package com.lucky.VehicleManagementSystem.standalone;

import com.lucky.VehicleManagementSystem.payload.ApiResponse;
import com.lucky.VehicleManagementSystem.utils.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception e){
        return ExceptionUtils.handleResponseException(e);
    }
}
