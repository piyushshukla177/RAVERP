package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class BookingPlotListModal implements Serializable {

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

    public static class Body extends BookingPlotListModal {
        @Expose
        @SerializedName("TotalDue")
        private int totaldue;
        @Expose
        @SerializedName("TotalPaid")
        private int totalpaid;
        @Expose
        @SerializedName("strSouth")
        private String strsouth;
        @Expose
        @SerializedName("strNorth")
        private String strnorth;
        @Expose
        @SerializedName("strWest")
        private String strwest;
        @Expose
        @SerializedName("strEast")
        private String streast;
        @Expose
        @SerializedName("strPlan")
        private String strplan;
        @Expose
        @SerializedName("intPlanTypeId")
        private int intplantypeid;
        @Expose
        @SerializedName("strDisplayName")
        private String strdisplayname;
        @Expose
        @SerializedName("AssocicationCode")
        private String associcationcode;
        @Expose
        @SerializedName("intFK_MemId")
        private int intfkMemid;
        @Expose
        @SerializedName("strAddress")
        private String straddress;
        @Expose
        @SerializedName("strCustId")
        private String strcustid;
        @Expose
        @SerializedName("strName")
        private String strname;
        @Expose
        @SerializedName("numericPlotAmount")
        private String numericplotamount;
        @Expose
        @SerializedName("floatPlotBookingAmount")
        private int floatplotbookingamount;
        @Expose
        @SerializedName("floatPlotArea")
        private int floatplotarea;
        @Expose
        @SerializedName("floatY")
        private int floaty;
        @Expose
        @SerializedName("floatX")
        private int floatx;
        @Expose
        @SerializedName("strPlotNo")
        private String strplotno;
        @Expose
        @SerializedName("intPlotId")
        private int intplotid;
        @Expose
        @SerializedName("strBlockName")
        private String strblockname;
        @Expose
        @SerializedName("intBlockId")
        private int intblockid;
        @Expose
        @SerializedName("ProjectState")
        private String projectstate;
        @Expose
        @SerializedName("ProjectCity")
        private String projectcity;
        @Expose
        @SerializedName("strProjectName")
        private String strprojectname;
        @Expose
        @SerializedName("intProjectId")
        private int intprojectid;
        @Expose
        @SerializedName("strCustAccNo")
        String strcustaccno;
        @Expose
        @SerializedName("bigIntBookingId")
        private int bigintbookingid;

        public int getTotaldue() {
            return totaldue;
        }

        public void setTotaldue(int totaldue) {
            this.totaldue = totaldue;
        }

        public int getTotalpaid() {
            return totalpaid;
        }

        public void setTotalpaid(int totalpaid) {
            this.totalpaid = totalpaid;
        }

        public String getStrsouth() {
            return strsouth;
        }

        public void setStrsouth(String strsouth) {
            this.strsouth = strsouth;
        }

        public String getStrnorth() {
            return strnorth;
        }

        public void setStrnorth(String strnorth) {
            this.strnorth = strnorth;
        }

        public String getStrwest() {
            return strwest;
        }

        public void setStrwest(String strwest) {
            this.strwest = strwest;
        }

        public String getStreast() {
            return streast;
        }

        public void setStreast(String streast) {
            this.streast = streast;
        }

        public String getStrplan() {
            return strplan;
        }

        public void setStrplan(String strplan) {
            this.strplan = strplan;
        }

        public int getIntplantypeid() {
            return intplantypeid;
        }

        public void setIntplantypeid(int intplantypeid) {
            this.intplantypeid = intplantypeid;
        }

        public String getStrdisplayname() {
            return strdisplayname;
        }

        public void setStrdisplayname(String strdisplayname) {
            this.strdisplayname = strdisplayname;
        }

        public String getAssocicationcode() {
            return associcationcode;
        }

        public void setAssocicationcode(String associcationcode) {
            this.associcationcode = associcationcode;
        }

        public int getIntfkMemid() {
            return intfkMemid;
        }

        public void setIntfkMemid(int intfkMemid) {
            this.intfkMemid = intfkMemid;
        }

        public String getStraddress() {
            return straddress;
        }

        public void setStraddress(String straddress) {
            this.straddress = straddress;
        }

        public String getStrcustid() {
            return strcustid;
        }

        public void setStrcustid(String strcustid) {
            this.strcustid = strcustid;
        }

        public String getStrname() {
            return strname;
        }

        public void setStrname(String strname) {
            this.strname = strname;
        }

        public String getNumericplotamount() {
            return numericplotamount;
        }

        public void setNumericplotamount(String numericplotamount) {
            this.numericplotamount = numericplotamount;
        }

        public int getFloatplotbookingamount() {
            return floatplotbookingamount;
        }

        public void setFloatplotbookingamount(int floatplotbookingamount) {
            this.floatplotbookingamount = floatplotbookingamount;
        }

        public int getFloatplotarea() {
            return floatplotarea;
        }

        public void setFloatplotarea(int floatplotarea) {
            this.floatplotarea = floatplotarea;
        }

        public int getFloaty() {
            return floaty;
        }

        public void setFloaty(int floaty) {
            this.floaty = floaty;
        }

        public int getFloatx() {
            return floatx;
        }

        public void setFloatx(int floatx) {
            this.floatx = floatx;
        }

        public String getStrplotno() {
            return strplotno;
        }

        public void setStrplotno(String strplotno) {
            this.strplotno = strplotno;
        }

        public int getIntplotid() {
            return intplotid;
        }

        public void setIntplotid(int intplotid) {
            this.intplotid = intplotid;
        }

        public String getStrblockname() {
            return strblockname;
        }

        public void setStrblockname(String strblockname) {
            this.strblockname = strblockname;
        }

        public int getIntblockid() {
            return intblockid;
        }

        public void setIntblockid(int intblockid) {
            this.intblockid = intblockid;
        }

        public String getProjectstate() {
            return projectstate;
        }

        public void setProjectstate(String projectstate) {
            this.projectstate = projectstate;
        }

        public String getProjectcity() {
            return projectcity;
        }

        public void setProjectcity(String projectcity) {
            this.projectcity = projectcity;
        }

        public String getStrprojectname() {
            return strprojectname;
        }

        public void setStrprojectname(String strprojectname) {
            this.strprojectname = strprojectname;
        }

        public int getIntprojectid() {
            return intprojectid;
        }

        public void setIntprojectid(int intprojectid) {
            this.intprojectid = intprojectid;
        }

        public String getStrcustaccno() {
            return strcustaccno;
        }

        public void setStrcustaccno(String strcustaccno) {
            this.strcustaccno = strcustaccno;
        }

        public int getBigintbookingid() {
            return bigintbookingid;
        }

        public void setBigintbookingid(int bigintbookingid) {
            this.bigintbookingid = bigintbookingid;
        }
    }
}
