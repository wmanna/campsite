package com.upgrade.campsite.exception;

public final class Constant {

    // API Errors

    public static final int REQUIRED_RESERVATION_CODE = 4001;
    public static final String REQUIRED_RESERVATION_CODE_MSG = "The reservation code is required.";

    public static final int RESERVATION_NOT_FOUND_ERROR_CODE = 4002;
    public static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation not found.";

    public static final int REQUIRED_RESERVATION_DATES_ERROR_CODE = 4003;
    public static final String REQUIRED_RESERVATION_DATES_ERROR_MSG = "Arrival and departure dates can not be null or empty.";

    public static final int INVALID_RESERVATION_DATES_ERROR_CODE = 4004;
    public static final String INVALID_RESERVATION_DATES_ERROR_MSG = "Invalid reservation dates.";

    public static final int RESERVATION_MAX_DURATION_ERROR_CODE = 4005;
    public static final String RESERVATION_MAX_DURATION_ERROR_MSG = "Reservation max. duration exceeded.";

    public static final int ARRIVAL_DATE_NOT_AVAILABLE_ERROR_CODE = 4006;
    public static final String ARRIVAL_DATE_NOT_AVAILABLE_ERROR_MSG = "The arrival date is not available.";

    public static final int NOT_AVAILABLE_DAYS_ERROR_CODE = 4007;
    public static final String NOT_AVAILABLE_DAYS_ERROR_MSG = "One or more days are not available.";

    public static final int USER_DATA_REQUIRED_ERROR_CODE = 4008;
    public static final String USER_DATA_REQUIRED_ERROR_MSG = "User full name and email are required.";

    // General Errors

    public static final String RESOURCE_LOCK_NOT_FOUND_MSG = "Resource lock not found.";
    public static final String RESOURCE_LOCKED_MSG = "Locked resource. Try it later.";
    public static final String UNKNOWN_ERROR_MSG = "Unknown error. Try it later.";

}
