package com.rav.raverp.data.model.api;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SiteVisitRequestStatusModel implements Serializable
{

    @SerializedName("strVehicleName")
    @Expose
    private String strVehicleName;
    @SerializedName("intVehicleNo")
    @Expose
    private Integer intVehicleNo;
    @SerializedName("OtherVehicleNo")
    @Expose
    private Integer otherVehicleNo;
    @SerializedName("DriverName")
    @Expose
    private Object driverName;
    @SerializedName("ManagerName")
    @Expose
    private Object managerName;
    @SerializedName("strPicUpLocation")
    @Expose
    private String strPicUpLocation;
    @SerializedName("strDropLocation")
    @Expose
    private String strDropLocation;
    @SerializedName("dtSiteVisitRequestedDate")
    @Expose
    private String dtSiteVisitRequestedDate;
    @SerializedName("strPlotNo")
    @Expose
    private String strPlotNo;
    @SerializedName("strRequesterName")
    @Expose
    private String strRequesterName;
    @SerializedName("strReqMobileNo")
    @Expose
    private String strReqMobileNo;
    @SerializedName("strReqEmailId")
    @Expose
    private String strReqEmailId;
    @SerializedName("IntSiteVisitRequestStatus")
    @Expose
    private Integer intSiteVisitRequestStatus;
    private final static long serialVersionUID = 2765430771425545345L;

    public String getStrVehicleName() {
        return strVehicleName;
    }

    public void setStrVehicleName(String strVehicleName) {
        this.strVehicleName = strVehicleName;
    }

    public Integer getIntVehicleNo() {
        return intVehicleNo;
    }

    public void setIntVehicleNo(Integer intVehicleNo) {
        this.intVehicleNo = intVehicleNo;
    }

    public Integer getOtherVehicleNo() {
        return otherVehicleNo;
    }

    public void setOtherVehicleNo(Integer otherVehicleNo) {
        this.otherVehicleNo = otherVehicleNo;
    }

    public Object getDriverName() {
        return driverName;
    }

    public void setDriverName(Object driverName) {
        this.driverName = driverName;
    }

    public Object getManagerName() {
        return managerName;
    }

    public void setManagerName(Object managerName) {
        this.managerName = managerName;
    }

    public String getStrPicUpLocation() {
        return strPicUpLocation;
    }

    public void setStrPicUpLocation(String strPicUpLocation) {
        this.strPicUpLocation = strPicUpLocation;
    }

    public String getStrDropLocation() {
        return strDropLocation;
    }

    public void setStrDropLocation(String strDropLocation) {
        this.strDropLocation = strDropLocation;
    }

    public String getDtSiteVisitRequestedDate() {
        return dtSiteVisitRequestedDate;
    }

    public void setDtSiteVisitRequestedDate(String dtSiteVisitRequestedDate) {
        this.dtSiteVisitRequestedDate = dtSiteVisitRequestedDate;
    }

    public String getStrPlotNo() {
        return strPlotNo;
    }

    public void setStrPlotNo(String strPlotNo) {
        this.strPlotNo = strPlotNo;
    }

    public String getStrRequesterName() {
        return strRequesterName;
    }

    public void setStrRequesterName(String strRequesterName) {
        this.strRequesterName = strRequesterName;
    }

    public String getStrReqMobileNo() {
        return strReqMobileNo;
    }

    public void setStrReqMobileNo(String strReqMobileNo) {
        this.strReqMobileNo = strReqMobileNo;
    }

    public String getStrReqEmailId() {
        return strReqEmailId;
    }

    public void setStrReqEmailId(String strReqEmailId) {
        this.strReqEmailId = strReqEmailId;
    }

    public Integer getIntSiteVisitRequestStatus() {
        return intSiteVisitRequestStatus;
    }

    public void setIntSiteVisitRequestStatus(Integer intSiteVisitRequestStatus) {
        this.intSiteVisitRequestStatus = intSiteVisitRequestStatus;
    }

}