package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPath {
    private static final String BASE_PATH = "/monitoring-service";
    public static final String AUTHENTICATION = BASE_PATH + "/authentication";
    public static final String REGISTRATION = BASE_PATH + "/registration";
    public static final String LOGOUT = BASE_PATH + "/logout";
}
