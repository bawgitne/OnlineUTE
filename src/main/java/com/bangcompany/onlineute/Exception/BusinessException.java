package com.bangcompany.onlineute.Exception;

//xu li loi ma code kho qua
public class BusinessException extends RuntimeException {
    private final String code;
    private final String userMessage;

    public BusinessException(String message) {
        super(message);
        this.userMessage = message;
        this.code = null;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.userMessage = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getUserMessage() {
        return userMessage;
    }
}