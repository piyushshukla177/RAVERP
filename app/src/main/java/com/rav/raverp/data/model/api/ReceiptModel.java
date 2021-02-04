package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class ReceiptModel implements Serializable {


    @Expose
    @SerializedName("body")
    private List<Body> body;
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Response")
    private String response;

    public List<Body> getBody() {
        return body;
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class Body {
        @Expose
        @SerializedName("dcPaidAmount")
        private int dcpaidamount;
        @Expose
        @SerializedName("dtAmountPaidDate")
        private String dtamountpaiddate;
        @Expose
        @SerializedName("bigintCashId")
        private int bigintcashid;
        @Expose
        @SerializedName("bigIntBookingId")
        private int bigintbookingid;

        public int getDcpaidamount() {
            return dcpaidamount;
        }

        public void setDcpaidamount(int dcpaidamount) {
            this.dcpaidamount = dcpaidamount;
        }

        public String getDtamountpaiddate() {
            return dtamountpaiddate;
        }

        public void setDtamountpaiddate(String dtamountpaiddate) {
            this.dtamountpaiddate = dtamountpaiddate;
        }

        public int getBigintcashid() {
            return bigintcashid;
        }

        public void setBigintcashid(int bigintcashid) {
            this.bigintcashid = bigintcashid;
        }

        public int getBigintbookingid() {
            return bigintbookingid;
        }

        public void setBigintbookingid(int bigintbookingid) {
            this.bigintbookingid = bigintbookingid;
        }
    }
}
