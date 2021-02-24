package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ChatModel {
    @Expose
    @SerializedName("body")
    private List<Body> body;
    @Expose
    @SerializedName("Response")
    private String response;
    @Expose
    @SerializedName("obj")
    private Obj obj;
    @Expose
    @SerializedName("Message")
    private String message;

    public List<Body> getBody() {
        return body;
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Body {
        @Expose
        @SerializedName("Name")
        private String name;
        @Expose
        @SerializedName("CreatedDate")
        private String createddate;
        @Expose
        @SerializedName("MessageBy")
        private String messageby;
        @Expose
        @SerializedName("ProfilePic")
        private String profilepic;
        @Expose
        @SerializedName("Attachment")
        private String attachment;
        @Expose
        @SerializedName("CreatedBy")
        private int createdby;
        @Expose
        @SerializedName("Message")
        private String message;
        @Expose
        @SerializedName("Fk_TicketNo")
        private String fkTicketno;
        @Expose
        @SerializedName("Pk_MsgId")
        private int pkMsgid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

        public String getMessageby() {
            return messageby;
        }

        public void setMessageby(String messageby) {
            this.messageby = messageby;
        }

        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public int getCreatedby() {
            return createdby;
        }

        public void setCreatedby(int createdby) {
            this.createdby = createdby;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFkTicketno() {
            return fkTicketno;
        }

        public void setFkTicketno(String fkTicketno) {
            this.fkTicketno = fkTicketno;
        }

        public int getPkMsgid() {
            return pkMsgid;
        }

        public void setPkMsgid(int pkMsgid) {
            this.pkMsgid = pkMsgid;
        }
    }

    public static class Obj {
        @Expose
        @SerializedName("LastUpdated")
        private String lastupdated;
        @Expose
        @SerializedName("Submitted")
        private String submitted;
        @Expose
        @SerializedName("Status")
        private String status;
        @Expose
        @SerializedName("Priority")
        private String priority;
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
        @SerializedName("ClaimTypeName")
        private String claimtypename;
        @Expose
        @SerializedName("strBranchName")
        private String strbranchname;
        @Expose
        @SerializedName("strBankName")
        private String strbankname;
        @Expose
        @SerializedName("IFSC")
        private String ifsc;
        @Expose
        @SerializedName("strPaymentType")
        private String strpaymenttype;
        @Expose
        @SerializedName("dtTransDate")
        private String dttransdate;
        @Expose
        @SerializedName("dcAmount")
        private int dcamount;
        @Expose
        @SerializedName("strTransactionNo")
        private String strtransactionno;
        @Expose
        @SerializedName("strAttachment")
        private String strattachment;
        @Expose
        @SerializedName("IsDeleted")
        private boolean isdeleted;
        @Expose
        @SerializedName("DeletedBy")
        private int deletedby;
        @Expose
        @SerializedName("UpdatedBy")
        private int updatedby;
        @Expose
        @SerializedName("CreatedDate")
        private String createddate;
        @Expose
        @SerializedName("CreatedBy")
        private int createdby;
        @Expose
        @SerializedName("IsActive")
        private boolean isactive;
        @Expose
        @SerializedName("Fk_ClosedBy")
        private int fkClosedby;
        @Expose
        @SerializedName("Query")
        private String query;
        @Expose
        @SerializedName("ClaimType")
        private int claimtype;
        @Expose
        @SerializedName("strDocument")
        private int strdocument;
        @Expose
        @SerializedName("DocumentType")
        private String documenttype;
        @Expose
        @SerializedName("intDocumentTypeId")
        private int intdocumenttypeid;
        @Expose
        @SerializedName("SubjectId")
        private int subjectid;
        @Expose
        @SerializedName("Subject")
        private String subject;
        @Expose
        @SerializedName("SupportFor")
        private String supportfor;
        @Expose
        @SerializedName("Pk_TicketNo")
        private String pkTicketno;

        public String getLastupdated() {
            return lastupdated;
        }

        public void setLastupdated(String lastupdated) {
            this.lastupdated = lastupdated;
        }

        public String getSubmitted() {
            return submitted;
        }

        public void setSubmitted(String submitted) {
            this.submitted = submitted;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

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

        public String getClaimtypename() {
            return claimtypename;
        }

        public void setClaimtypename(String claimtypename) {
            this.claimtypename = claimtypename;
        }

        public String getStrbranchname() {
            return strbranchname;
        }

        public void setStrbranchname(String strbranchname) {
            this.strbranchname = strbranchname;
        }

        public String getStrbankname() {
            return strbankname;
        }

        public void setStrbankname(String strbankname) {
            this.strbankname = strbankname;
        }

        public String getIfsc() {
            return ifsc;
        }

        public void setIfsc(String ifsc) {
            this.ifsc = ifsc;
        }

        public String getStrpaymenttype() {
            return strpaymenttype;
        }

        public void setStrpaymenttype(String strpaymenttype) {
            this.strpaymenttype = strpaymenttype;
        }

        public String getDttransdate() {
            return dttransdate;
        }

        public void setDttransdate(String dttransdate) {
            this.dttransdate = dttransdate;
        }

        public int getDcamount() {
            return dcamount;
        }

        public void setDcamount(int dcamount) {
            this.dcamount = dcamount;
        }

        public String getStrtransactionno() {
            return strtransactionno;
        }

        public void setStrtransactionno(String strtransactionno) {
            this.strtransactionno = strtransactionno;
        }

        public String getStrattachment() {
            return strattachment;
        }

        public void setStrattachment(String strattachment) {
            this.strattachment = strattachment;
        }

        public boolean getIsdeleted() {
            return isdeleted;
        }

        public void setIsdeleted(boolean isdeleted) {
            this.isdeleted = isdeleted;
        }

        public int getDeletedby() {
            return deletedby;
        }

        public void setDeletedby(int deletedby) {
            this.deletedby = deletedby;
        }

        public int getUpdatedby() {
            return updatedby;
        }

        public void setUpdatedby(int updatedby) {
            this.updatedby = updatedby;
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

        public boolean getIsactive() {
            return isactive;
        }

        public void setIsactive(boolean isactive) {
            this.isactive = isactive;
        }

        public int getFkClosedby() {
            return fkClosedby;
        }

        public void setFkClosedby(int fkClosedby) {
            this.fkClosedby = fkClosedby;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public int getClaimtype() {
            return claimtype;
        }

        public void setClaimtype(int claimtype) {
            this.claimtype = claimtype;
        }

        public int getStrdocument() {
            return strdocument;
        }

        public void setStrdocument(int strdocument) {
            this.strdocument = strdocument;
        }

        public String getDocumenttype() {
            return documenttype;
        }

        public void setDocumenttype(String documenttype) {
            this.documenttype = documenttype;
        }

        public int getIntdocumenttypeid() {
            return intdocumenttypeid;
        }

        public void setIntdocumenttypeid(int intdocumenttypeid) {
            this.intdocumenttypeid = intdocumenttypeid;
        }

        public int getSubjectid() {
            return subjectid;
        }

        public void setSubjectid(int subjectid) {
            this.subjectid = subjectid;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSupportfor() {
            return supportfor;
        }

        public void setSupportfor(String supportfor) {
            this.supportfor = supportfor;
        }

        public String getPkTicketno() {
            return pkTicketno;
        }

        public void setPkTicketno(String pkTicketno) {
            this.pkTicketno = pkTicketno;
        }
    }
}
