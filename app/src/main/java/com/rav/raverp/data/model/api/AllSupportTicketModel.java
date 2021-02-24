package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class AllSupportTicketModel implements Serializable {


    @Expose
    @SerializedName("LastUpdated")
    private String lastupdated;
    @Expose
    @SerializedName("Status")
    private String status;
    @Expose
    @SerializedName("Subject")
    private String subject;
    @Expose
    @SerializedName("SupportType")
    private String supporttype;
    @Expose
    @SerializedName("SupportFor")
    private String supportfor;
    @Expose
    @SerializedName("TicketNo")
    private String ticketno;
    @Expose
    @SerializedName("TotalRecords")
    private int totalrecords;
    @Expose
    @SerializedName("RowNum")
    private int rownum;

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSupporttype() {
        return supporttype;
    }

    public void setSupporttype(String supporttype) {
        this.supporttype = supporttype;
    }

    public String getSupportfor() {
        return supportfor;
    }

    public void setSupportfor(String supportfor) {
        this.supportfor = supportfor;
    }

    public String getTicketno() {
        return ticketno;
    }

    public void setTicketno(String ticketno) {
        this.ticketno = ticketno;
    }

    public int getTotalrecords() {
        return totalrecords;
    }

    public void setTotalrecords(int totalrecords) {
        this.totalrecords = totalrecords;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
}
