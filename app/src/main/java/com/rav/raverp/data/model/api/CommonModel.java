package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class CommonModel {

    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Response")
    private String response;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
