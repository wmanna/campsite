package com.upgrade.campsite;

public final class Constant {

    public static final String API_NAME = "Campsite API";

    // API Errors
    public static final int RESERVATION_NOT_FOUND_CODE = 4001;
    public static final String RESERVATION_NOT_FOUND_MSG = "Reservation not found.";

    public static final int REQUIRED_RESERVATION_CODE = 4008;
    public static final String REQUIRED_RESERVATION_CODE_MSG = "The reservation code is required.";

    public static final int USER_DATA_REQUIRED_ERROR_CODE = 4010;
    public static final String USER_DATA_REQUIRED_ERROR_MSG = "User full name and email are required.";

    // General Errors
    public static final String RESOURCE_LOCK_NOT_FOUND_MSG = "Resource lock not found.";
    public static final String RESOURCE_LOCKED_MSG = "Locked resource. Try it later.";
    public static final String UNKNOWN_ERROR_MSG = "Unknown error (500). Try it later.";

}
