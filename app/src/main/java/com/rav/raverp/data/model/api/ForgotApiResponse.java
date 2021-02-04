package com.rav.raverp.data.model.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import retrofit2.http.Body;

public class ForgotApiResponse implements Serializable
{

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("body")
    @Expose
    private List<ForgotpasswordModel> body = null;
    private final static long serialVersionUID = -2175503242428883840L;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ForgotpasswordModel> getBody() {
        return body;
    }

    public void setBody(List<ForgotpasswordModel> body) {
        this.body = body;
    }

}