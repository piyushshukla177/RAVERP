package com.rav.raverp.data.model.api;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PlotBookingStatus implements Serializable
{

    @SerializedName("intPlotBookingStatusId")
    @Expose
    private Integer intPlotBookingStatusId;
    @SerializedName("strPlotBookingStatus")
    @Expose
    private String strPlotBookingStatus;
    private final static long serialVersionUID = -4821603159406199708L;

    public Integer getIntPlotBookingStatusId() {
        return intPlotBookingStatusId;
    }

    public void setIntPlotBookingStatusId(Integer intPlotBookingStatusId) {
        this.intPlotBookingStatusId = intPlotBookingStatusId;
    }

    public String getStrPlotBookingStatus(String s) {
        return strPlotBookingStatus;
    }

    public void setStrPlotBookingStatus(String strPlotBookingStatus) {
        this.strPlotBookingStatus = strPlotBookingStatus;
    }
    public String toString() {
        return strPlotBookingStatus;
    }


}