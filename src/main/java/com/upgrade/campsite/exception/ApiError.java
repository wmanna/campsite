package com.upgrade.campsite.exception;

public class ApiError {

    private int errorCode;
    private String errorMessage;

    public ApiError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
