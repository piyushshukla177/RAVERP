package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentModeTypeModel implements Serializable {
    @Expose
    @SerializedName("intIsDelated")
    private int intisdelated;
    @Expose
    @SerializedName("dtCreatedDate")
    private String dtcreateddate;
    @Expose
    @SerializedName("strCreatedBy")
    private String strcreatedby;
    @Expose
    @SerializedName("strPaymenType")
    private String strpaymentype;
    @Expose
    @SerializedName("intPaymentTypeId")
    private int intpaymenttypeid;

    public int getIntisdelated() {
        return intisdelated;
    }

    public void setIntisdelated(int intisdelated) {
        this.intisdelated = intisdelated;
    }

    public String getDtcreateddate() {
        return dtcreateddate;
    }

    public void setDtcreateddate(String dtcreateddate) {
        this.dtcreateddate = dtcreateddate;
    }

    public String getStrcreatedby() {
        return strcreatedby;
    }

    public void setStrcreatedby(String strcreatedby) {
        this.strcreatedby = strcreatedby;
    }

    public String getStrpaymentype() {
        return strpaymentype;
    }

    public void setStrpaymentype(String strpaymentype) {
        this.strpaymentype = strpaymentype;
    }

    public int getIntpaymenttypeid() {
        return intpaymenttypeid;
    }

    public void setIntpaymenttypeid(int intpaymenttypeid) {
        this.intpaymenttypeid = intpaymenttypeid;
    }

    public String toString() {
        return strpaymentype;
    }
}
