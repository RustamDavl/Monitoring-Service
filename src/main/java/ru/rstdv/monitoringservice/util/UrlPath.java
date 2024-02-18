package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPath {
    private static final String BASE_PATH = "/monitoring-service/api/v1";
    public static final String AUTHENTICATION = BASE_PATH + "/users/authenticate";
    public static final String REGISTRATION = BASE_PATH + "/users/register";
    public static final String LOGOUT = BASE_PATH + "/users/logout";
    public static final String SEND_WATER_METER_READING = BASE_PATH + "/water-meter-readings";
    public static final String SEND_THERMAL_METER_READING = BASE_PATH + "/thermal-meter-readings";
}
