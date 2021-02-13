package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class IFSCCodeModel {

    @Expose
    @SerializedName("IFSC")
    private String ifsc;
    @Expose
    @SerializedName("BANKCODE")
    private String bankcode;
    @Expose
    @SerializedName("BANK")
    private String bank;
    @Expose
    @SerializedName("MICR")
    private String micr;
    @Expose
    @SerializedName("ADDRESS")
    private String address;
    @Expose
    @SerializedName("CONTACT")
    private String contact;
    @Expose
    @SerializedName("CENTRE")
    private String centre;
    @Expose
    @SerializedName("RTGS")
    private boolean rtgs;
    @Expose
    @SerializedName("IMPS")
    private boolean imps;
    @Expose
    @SerializedName("NEFT")
    private boolean neft;
    @Expose
    @SerializedName("BRANCH")
    private String branch;
    @Expose
    @SerializedName("STATE")
    private String state;
    @Expose
    @SerializedName("CITY")
    private String city;
    @Expose
    @SerializedName("SWIFT")
    private String swift;
    @Expose
    @SerializedName("UPI")
    private boolean upi;
    @Expose
    @SerializedName("DISTRICT")
    private String district;

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public boolean getRtgs() {
        return rtgs;
    }

    public void setRtgs(boolean rtgs) {
        this.rtgs = rtgs;
    }

    public boolean getImps() {
        return imps;
    }

    public void setImps(boolean imps) {
        this.imps = imps;
    }

    public boolean getNeft() {
        return neft;
    }

    public void setNeft(boolean neft) {
        this.neft = neft;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public boolean getUpi() {
        return upi;
    }

    public void setUpi(boolean upi) {
        this.upi = upi;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
