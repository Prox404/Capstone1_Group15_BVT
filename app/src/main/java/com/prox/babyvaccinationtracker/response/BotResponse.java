package com.prox.babyvaccinationtracker.response;

import com.google.gson.annotations.SerializedName;

public class BotResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
