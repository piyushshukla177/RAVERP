package com.rav.raverp.data.model.api;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletAccessResponse implements Serializable
{

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("walletPinStatus")
    @Expose
    private Boolean walletPinStatus;
    private final static long serialVersionUID = 8123726033089984061L;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getWalletPinStatus() {
        return walletPinStatus;
    }

    public void setWalletPinStatus(Boolean walletPinStatus) {
        this.walletPinStatus = walletPinStatus;
    }

}