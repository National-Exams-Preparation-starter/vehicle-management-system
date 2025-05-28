package com.lucky.VehicleManagementSystem.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
// https://github.com/cielo-b/eucl-api
public class Mapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static ModelMapper getMapper() {
        return modelMapper;
    }
}
