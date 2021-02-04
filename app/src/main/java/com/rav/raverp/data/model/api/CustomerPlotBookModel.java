package com.rav.raverp.data.model.api;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerPlotBookModel implements Serializable
{

    @SerializedName("strName")
    @Expose
    private String strName;
    @SerializedName("bigIntCustomerId")
    @Expose
    private Integer bigIntCustomerId;
    private final static long serialVersionUID = -6993249417886122856L;

    public String getStrName(String s) {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Integer getBigIntCustomerId() {
        return bigIntCustomerId;
    }

    public void setBigIntCustomerId(Integer bigIntCustomerId) {
        this.bigIntCustomerId = bigIntCustomerId;
    }
    @Override
    public String toString() {
        return strName;
    }
}
