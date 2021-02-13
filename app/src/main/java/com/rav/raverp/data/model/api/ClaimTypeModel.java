package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClaimTypeModel implements Serializable {

    @Expose
    @SerializedName("isdeleted")
    private boolean isdeleted;
    @Expose
    @SerializedName("strClaimTypeName")
    private String strclaimtypename;
    @Expose
    @SerializedName("intClaimTypeId")
    private int intclaimtypeid;

    public boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getStrclaimtypename() {
        return strclaimtypename;
    }

    public void setStrclaimtypename(String strclaimtypename) {
        this.strclaimtypename = strclaimtypename;
    }

    public int getIntclaimtypeid() {
        return intclaimtypeid;
    }

    public void setIntclaimtypeid(int intclaimtypeid) {
        this.intclaimtypeid = intclaimtypeid;
    }

    @Override
    public String toString() {
        return strclaimtypename;
    }
}
