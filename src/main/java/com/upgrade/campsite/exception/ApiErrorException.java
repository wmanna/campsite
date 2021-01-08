package com.upgrade.campsite.exception;

public class ApiErrorException extends Exception {

    private int apiErrorCode;

    public ApiErrorException(int apiErrorCode, String errorMessage) {
        super(errorMessage);
        this.apiErrorCode = apiErrorCode;
    }

    public int getApiErrorCode() {
        return apiErrorCode;
    }
}
