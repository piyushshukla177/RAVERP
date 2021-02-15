package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class PendingClosedTicketModel {


    @Expose
    @SerializedName("body")
    private List<Body> body;
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Response")
    private String response;
    @Expose
    @SerializedName("Count")
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class Body {
        @Expose
        @SerializedName("ProfilePic")
        private String profilepic;
        @Expose
        @SerializedName("MobileNo")
        private String mobileno;
        @Expose
        @SerializedName("DisplayName")
        private String displayname;
        @Expose
        @SerializedName("LoginId")
        private String loginid;
        @Expose
        @SerializedName("dtTransDate")
        private String dttransdate;
        @Expose
        @SerializedName("ClaimTypeName")
        private String claimtypename;
        @Expose
        @SerializedName("strPaymentType")
        private String strpaymenttype;
        @Expose
        @SerializedName("strBankName")
        private String strbankname;
        @Expose
        @SerializedName("strBranchName")
        private String strbranchname;
        @Expose
        @SerializedName("IFSC")
        private String ifsc;
        @Expose
        @SerializedName("strSubjectName")
        private String strsubjectname;
        @Expose
        @SerializedName("Fk_ClosedBy")
        private int fkClosedby;
        @Expose
        @SerializedName("IsActive")
        private boolean isactive;
        @Expose
        @SerializedName("CreatedDate")
        private String createddate;
        @Expose
        @SerializedName("CreatedBy")
        private int createdby;
        @Expose
        @SerializedName("Query")
        private String query;
        @Expose
        @SerializedName("Pk_TicketNo")
        private String pkTicketno;

        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getLoginid() {
            return loginid;
        }

        public void setLoginid(String loginid) {
            this.loginid = loginid;
        }

        public String getDttransdate() {
            return dttransdate;
        }

        public void setDttransdate(String dttransdate) {
            this.dttransdate = dttransdate;
        }

        public String getClaimtypename() {
            return claimtypename;
        }

        public void setClaimtypename(String claimtypename) {
            this.claimtypename = claimtypename;
        }

        public String getStrpaymenttype() {
            return strpaymenttype;
        }

        public void setStrpaymenttype(String strpaymenttype) {
            this.strpaymenttype = strpaymenttype;
        }

        public String getStrbankname() {
            return strbankname;
        }

        public void setStrbankname(String strbankname) {
            this.strbankname = strbankname;
        }

        public String getStrbranchname() {
            return strbranchname;
        }

        public void setStrbranchname(String strbranchname) {
            this.strbranchname = strbranchname;
        }

        public String getIfsc() {
            return ifsc;
        }

        public void setIfsc(String ifsc) {
            this.ifsc = ifsc;
        }

        public String getStrsubjectname() {
            return strsubjectname;
        }

        public void setStrsubjectname(String strsubjectname) {
            this.strsubjectname = strsubjectname;
        }

        public int getFkClosedby() {
            return fkClosedby;
        }

        public void setFkClosedby(int fkClosedby) {
            this.fkClosedby = fkClosedby;
        }

        public boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(boolean isactive) {
            this.isactive = isactive;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

        public int getCreatedby() {
            return createdby;
        }

        public void setCreatedby(int createdby) {
            this.createdby = createdby;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getPkTicketno() {
            return pkTicketno;
        }

        public void setPkTicketno(String pkTicketno) {
            this.pkTicketno = pkTicketno;
        }
    }
}
