package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenderModel implements Serializable {

    @SerializedName("intGenderId")
    @Expose
    private Integer intGenderId;
    @SerializedName("strGender")
    @Expose
    private String strGender;

    private final static long serialVersionUID = -2678967743025114647L;

    public Integer getIntGenderId() {
        return intGenderId;
    }

    public void setIntGenderId(Integer intGenderId) {
        this.intGenderId = intGenderId;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    @Override
    public String toString() {
        return strGender;
    }
}