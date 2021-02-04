package com.rav.raverp.data.model.api;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlotBookingPlan implements Serializable
{

    @SerializedName("intPlanTypeId")
    @Expose
    private Integer intPlanTypeId;
    @SerializedName("strPlan")
    @Expose
    private String strPlan;
    private final static long serialVersionUID = 2472739925041237877L;

    public Integer getIntPlanTypeId() {
        return intPlanTypeId;
    }

    public void setIntPlanTypeId(Integer intPlanTypeId) {
        this.intPlanTypeId = intPlanTypeId;
    }

    public String getStrPlan() {
        return strPlan;
    }

    public void setStrPlan(String strPlan) {
        this.strPlan = strPlan;
    }
    public String toString() {
        return strPlan;
    }

}
