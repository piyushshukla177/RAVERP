package com.rav.raverp.data.model.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActivityLogModel implements Serializable
{

    @SerializedName("dtLoginDate")
    @Expose
    private String dtLoginDate;
    private final static long serialVersionUID = 7892467949126123181L;

    public String getDtLoginDate() {
        return dtLoginDate;
    }

    public void setDtLoginDate(String dtLoginDate) {
        this.dtLoginDate = dtLoginDate;
    }

}