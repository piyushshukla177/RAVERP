package com.rav.raverp.data.model.api;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AssociateWalletAmount implements Serializable
{

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("AssociateWalletAmounts")
    @Expose
    private String associateWalletAmounts;
    private final static long serialVersionUID = 9073045623973476549L;

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

    public String getAssociateWalletAmounts() {
        return associateWalletAmounts;
    }

    public void setAssociateWalletAmounts(String associateWalletAmounts) {
        this.associateWalletAmounts = associateWalletAmounts;
    }

}