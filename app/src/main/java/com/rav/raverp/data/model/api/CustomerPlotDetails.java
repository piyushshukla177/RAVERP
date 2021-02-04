package com.rav.raverp.data.model.api;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerPlotDetails implements Serializable
{

    @SerializedName("bigIntBookingId")
    @Expose
    private Integer bigIntBookingId;
    @SerializedName("intProjectId")
    @Expose
    private Integer intProjectId;
    @SerializedName("strProjectName")
    @Expose
    private String strProjectName;
    @SerializedName("intBlockId")
    @Expose
    private Integer intBlockId;
    @SerializedName("strBlockName")
    @Expose
    private String strBlockName;
    @SerializedName("strPlotNo")
    @Expose
    private String strPlotNo;
    @SerializedName("bigIntCustomerId")
    @Expose
    private Integer bigIntCustomerId;
    @SerializedName("strName")
    @Expose
    private String strName;
    @SerializedName("floatRateSqr")
    @Expose
    private String floatRateSqr;
    @SerializedName("Height")
    @Expose
    private Double height;
    @SerializedName("Widht")
    @Expose
    private Double widht;
    @SerializedName("floatPlotArea")
    @Expose
    private Double floatPlotArea;
    @SerializedName("FirstPayAmount")
    @Expose
    private Double firstPayAmount;
    @SerializedName("strBookingPlotStatus")
    @Expose
    private String strBookingPlotStatus;

    @SerializedName("strCustAccNo")
    @Expose
    private String strCustAccNo;

    @SerializedName("strPlotBookingStatus")
    @Expose
    private String strPlotBookingStatus;

    @SerializedName("dcPlotBooingAmount")
    @Expose
    private String dcPlotBooingAmount;

    private final static long serialVersionUID = 4023548991060468367L;

    public String getStrCustAccNo() {
        return strCustAccNo;
    }

    public void setStrCustAccNo(String strCustAccNo) {
        this.strCustAccNo = strCustAccNo;
    }
    public String getStrPlotBookingStatus() {
        return strPlotBookingStatus;
    }

    public void setStrPlotBookingStatus(String strPlotBookingStatus) {
        this.strPlotBookingStatus = strPlotBookingStatus;
    }

    public String getDcPlotBooingAmount() {
        return dcPlotBooingAmount;
    }

    public void setDcPlotBooingAmount(String dcPlotBooingAmount) {
        this.dcPlotBooingAmount = dcPlotBooingAmount;
    }

    public Integer getBigIntBookingId() {
        return bigIntBookingId;
    }

    public void setBigIntBookingId(Integer bigIntBookingId) {
        this.bigIntBookingId = bigIntBookingId;
    }

    public Integer getIntProjectId() {
        return intProjectId;
    }

    public void setIntProjectId(Integer intProjectId) {
        this.intProjectId = intProjectId;
    }

    public String getStrProjectName() {
        return strProjectName;
    }

    public void setStrProjectName(String strProjectName) {
        this.strProjectName = strProjectName;
    }

    public Integer getIntBlockId() {
        return intBlockId;
    }

    public void setIntBlockId(Integer intBlockId) {
        this.intBlockId = intBlockId;
    }

    public String getStrBlockName() {
        return strBlockName;
    }

    public void setStrBlockName(String strBlockName) {
        this.strBlockName = strBlockName;
    }

    public String getStrPlotNo() {
        return strPlotNo;
    }

    public void setStrPlotNo(String strPlotNo) {
        this.strPlotNo = strPlotNo;
    }

    public Integer getBigIntCustomerId() {
        return bigIntCustomerId;
    }

    public void setBigIntCustomerId(Integer bigIntCustomerId) {
        this.bigIntCustomerId = bigIntCustomerId;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getFloatRateSqr() {
        return floatRateSqr;
    }

    public void setFloatRateSqr(String floatRateSqr) {
        this.floatRateSqr = floatRateSqr;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidht() {
        return widht;
    }

    public void setWidht(Double widht) {
        this.widht = widht;
    }

    public Double getFloatPlotArea() {
        return floatPlotArea;
    }

    public void setFloatPlotArea(Double floatPlotArea) {
        this.floatPlotArea = floatPlotArea;
    }

    public Double getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(Double firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getStrBookingPlotStatus() {
        return strBookingPlotStatus;
    }

    public void setStrBookingPlotStatus(String strBookingPlotStatus) {
        this.strBookingPlotStatus = strBookingPlotStatus;
    }

}