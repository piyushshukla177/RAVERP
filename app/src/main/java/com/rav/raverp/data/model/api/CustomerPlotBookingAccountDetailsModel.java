package com.rav.raverp.data.model.api;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerPlotBookingAccountDetailsModel implements Serializable
{

    @SerializedName("bigIntCustAccId")
    @Expose
    private Integer bigIntCustAccId;
    @SerializedName("bigIntCustId")
    @Expose
    private Integer bigIntCustId;
    @SerializedName("strCustAccNo")
    @Expose
    private String strCustAccNo;
    @SerializedName("bigIntPlotBookingId")
    @Expose
    private Integer bigIntPlotBookingId;
    @SerializedName("dtCreatedOn")
    @Expose
    private String dtCreatedOn;
    @SerializedName("bigIntBookingId")
    @Expose
    private Integer bigIntBookingId;
    @SerializedName("intFK_MemId")
    @Expose
    private Integer intFKMemId;
    @SerializedName("strFirstName")
    @Expose
    private String strFirstName;
    @SerializedName("intPlanTypeId")
    @Expose
    private Integer intPlanTypeId;
    @SerializedName("strPlan")
    @Expose
    private String strPlan;
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
    @SerializedName("intPlotId")
    @Expose
    private Integer intPlotId;
    @SerializedName("strPlotNo")
    @Expose
    private String strPlotNo;
    @SerializedName("floatX")
    @Expose
    private Double floatX;
    @SerializedName("floatY")
    @Expose
    private Double floatY;
    @SerializedName("floatPlotArea")
    @Expose
    private Double floatPlotArea;
    @SerializedName("bigIntBookingId1")
    @Expose
    private Integer bigIntBookingId1;
    @SerializedName("dcPlotBooingAmount")
    @Expose
    private Double dcPlotBooingAmount;
    @SerializedName("numericPlotAmount")
    @Expose
    private Double numericPlotAmount;
    @SerializedName("dtPlotBookingDate")
    @Expose
    private String dtPlotBookingDate;
    @SerializedName("intPlotBookingStatusId")
    @Expose
    private Integer intPlotBookingStatusId;
    @SerializedName("strPlotBookingStatus")
    @Expose
    private String strPlotBookingStatus;
    private final static long serialVersionUID = 1150657820300197775L;

    public Integer getBigIntCustAccId() {
        return bigIntCustAccId;
    }

    public void setBigIntCustAccId(Integer bigIntCustAccId) {
        this.bigIntCustAccId = bigIntCustAccId;
    }

    public Integer getBigIntCustId() {
        return bigIntCustId;
    }

    public void setBigIntCustId(Integer bigIntCustId) {
        this.bigIntCustId = bigIntCustId;
    }

    public String getStrCustAccNo() {
        return strCustAccNo;
    }

    public void setStrCustAccNo(String strCustAccNo) {
        this.strCustAccNo = strCustAccNo;
    }

    public Integer getBigIntPlotBookingId() {
        return bigIntPlotBookingId;
    }

    public void setBigIntPlotBookingId(Integer bigIntPlotBookingId) {
        this.bigIntPlotBookingId = bigIntPlotBookingId;
    }

    public String getDtCreatedOn() {
        return dtCreatedOn;
    }

    public void setDtCreatedOn(String dtCreatedOn) {
        this.dtCreatedOn = dtCreatedOn;
    }

    public Integer getBigIntBookingId() {
        return bigIntBookingId;
    }

    public void setBigIntBookingId(Integer bigIntBookingId) {
        this.bigIntBookingId = bigIntBookingId;
    }

    public Integer getIntFKMemId() {
        return intFKMemId;
    }

    public void setIntFKMemId(Integer intFKMemId) {
        this.intFKMemId = intFKMemId;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

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

    public Integer getIntPlotId() {
        return intPlotId;
    }

    public void setIntPlotId(Integer intPlotId) {
        this.intPlotId = intPlotId;
    }

    public String getStrPlotNo() {
        return strPlotNo;
    }

    public void setStrPlotNo(String strPlotNo) {
        this.strPlotNo = strPlotNo;
    }

    public Double getFloatX() {
        return floatX;
    }

    public void setFloatX(Double floatX) {
        this.floatX = floatX;
    }

    public Double getFloatY() {
        return floatY;
    }

    public void setFloatY(Double floatY) {
        this.floatY = floatY;
    }

    public Double getFloatPlotArea() {
        return floatPlotArea;
    }

    public void setFloatPlotArea(Double floatPlotArea) {
        this.floatPlotArea = floatPlotArea;
    }

    public Integer getBigIntBookingId1() {
        return bigIntBookingId1;
    }

    public void setBigIntBookingId1(Integer bigIntBookingId1) {
        this.bigIntBookingId1 = bigIntBookingId1;
    }

    public Double getDcPlotBooingAmount() {
        return dcPlotBooingAmount;
    }

    public void setDcPlotBooingAmount(Double dcPlotBooingAmount) {
        this.dcPlotBooingAmount = dcPlotBooingAmount;
    }

    public Double getNumericPlotAmount() {
        return numericPlotAmount;
    }

    public void setNumericPlotAmount(Double numericPlotAmount) {
        this.numericPlotAmount = numericPlotAmount;
    }

    public String getDtPlotBookingDate() {
        return dtPlotBookingDate;
    }

    public void setDtPlotBookingDate(String dtPlotBookingDate) {
        this.dtPlotBookingDate = dtPlotBookingDate;
    }

    public Integer getIntPlotBookingStatusId() {
        return intPlotBookingStatusId;
    }

    public void setIntPlotBookingStatusId(Integer intPlotBookingStatusId) {
        this.intPlotBookingStatusId = intPlotBookingStatusId;
    }

    public String getStrPlotBookingStatus() {
        return strPlotBookingStatus;
    }

    public void setStrPlotBookingStatus(String strPlotBookingStatus) {
        this.strPlotBookingStatus = strPlotBookingStatus;

    }}