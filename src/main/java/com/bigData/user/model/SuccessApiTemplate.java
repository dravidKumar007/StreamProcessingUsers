package com.bigData.user.model;

public class SuccessApiTemplate extends RestapiTemp{

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SuccessApiTemplate(String message, String token) {
        this.message = message;
        this.token = token;
    }

    String message;
    String token;
}
