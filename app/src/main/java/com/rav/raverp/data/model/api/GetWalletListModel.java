package com.rav.raverp.data.model.api;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWalletListModel implements Serializable
{

    @SerializedName("bigintWalletId")
    @Expose
    private Integer bigintWalletId;
    @SerializedName("strFirstName")
    @Expose
    private String strFirstName;
    @SerializedName("bigIntMemberId")
    @Expose
    private Integer bigIntMemberId;
    @SerializedName("dcAmount")
    @Expose
    private Double dcAmount;
    @SerializedName("intApproveStatus")
    @Expose
    private Integer intApproveStatus;
    @SerializedName("strPaymentType")
    @Expose
    private String strPaymentType;
    @SerializedName("strUTRNo")
    @Expose
    private String strUTRNo;
    @SerializedName("strNFTNo")
    @Expose
    private String strNFTNo;
    @SerializedName("WalletDate")
    @Expose
    private String walletDate;
    @SerializedName("TransNo")
    @Expose
    private String transNo;
    @SerializedName("strMobileNo")
    @Expose
    private String strMobileNo;
    @SerializedName("intRoleId")
    @Expose
    private Integer intRoleId;
    @SerializedName("intVerifiedStatusByAccountant")
    @Expose
    private Object intVerifiedStatusByAccountant;
    private final static long serialVersionUID = 6835626612206124408L;

    public Integer getBigintWalletId() {
        return bigintWalletId;
    }

    public void setBigintWalletId(Integer bigintWalletId) {
        this.bigintWalletId = bigintWalletId;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public Integer getBigIntMemberId() {
        return bigIntMemberId;
    }

    public void setBigIntMemberId(Integer bigIntMemberId) {
        this.bigIntMemberId = bigIntMemberId;
    }

    public Double getDcAmount() {
        return dcAmount;
    }

    public void setDcAmount(Double dcAmount) {
        this.dcAmount = dcAmount;
    }

    public Integer getIntApproveStatus() {
        return intApproveStatus;
    }

    public void setIntApproveStatus(Integer intApproveStatus) {
        this.intApproveStatus = intApproveStatus;
    }

    public String getStrPaymentType() {
        return strPaymentType;
    }

    public void setStrPaymentType(String strPaymentType) {
        this.strPaymentType = strPaymentType;
    }

    public String getStrUTRNo() {
        return strUTRNo;
    }

    public void setStrUTRNo(String strUTRNo) {
        this.strUTRNo = strUTRNo;
    }

    public String getStrNFTNo() {
        return strNFTNo;
    }

    public void setStrNFTNo(String strNFTNo) {
        this.strNFTNo = strNFTNo;
    }

    public String getWalletDate() {
        return walletDate;
    }

    public void setWalletDate(String walletDate) {
        this.walletDate = walletDate;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getStrMobileNo() {
        return strMobileNo;
    }

    public void setStrMobileNo(String strMobileNo) {
        this.strMobileNo = strMobileNo;
    }

    public Integer getIntRoleId() {
        return intRoleId;
    }

    public void setIntRoleId(Integer intRoleId) {
        this.intRoleId = intRoleId;
    }

    public Object getIntVerifiedStatusByAccountant() {
        return intVerifiedStatusByAccountant;
    }

    public void setIntVerifiedStatusByAccountant(Object intVerifiedStatusByAccountant) {
        this.intVerifiedStatusByAccountant = intVerifiedStatusByAccountant;
    }

}