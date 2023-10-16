package com.prox.babyvaccinationtracker.response;

import com.google.gson.annotations.SerializedName;

public class UploadImageResponse {
    @SerializedName("data")
    private Data data;

    @SerializedName("error")
    private String error;

    public String getImageLink() {
        return data.getUrl();
    }

    public String getError() {
        return error;
    }
}

class Data {
    public Data(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    // Getters và setters
    // ...
}
