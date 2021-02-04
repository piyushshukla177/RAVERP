package com.rav.raverp.data.model.api;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SiteVisitRequestName implements Serializable
{

    @SerializedName("strRequesterName")
    @Expose
    private String strRequesterName;
    private final static long serialVersionUID = -6532926650864088336L;

    public String getStrRequesterName() {
        return strRequesterName;
    }

    public void setStrRequesterName(String strRequesterName) {
        this.strRequesterName = strRequesterName;
    }

    @Override
    public String toString() {
        return strRequesterName;
    }

}
