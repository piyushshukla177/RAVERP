package com.rav.raverp.data.model.api;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletAmountListModel implements Serializable
{

    @SerializedName("RPlantinumWalletAmounts")
    @Expose
    private String  rPlantinumWalletAmounts;
    private final static long serialVersionUID = 1241036624210539599L;

    public String getRPlantinumWalletAmounts() {
        return rPlantinumWalletAmounts;
    }

    public void setRPlantinumWalletAmounts(String rPlantinumWalletAmounts) {
        this.rPlantinumWalletAmounts = rPlantinumWalletAmounts;
    }



}
