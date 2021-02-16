package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ChatModel {

    @Expose
    @SerializedName("body")
    private List<Body> body;
    @Expose
    @SerializedName("obj")
    private Obj obj;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class Body {
        @Expose
        @SerializedName("ProfilePic")
        private String profilepic;
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
        @SerializedName("Message")
        private String message;
        @Expose
        @SerializedName("Fk_TicketNo")
        private String fkTicketno;
        @Expose
        @SerializedName("Pk_MsgId")
        private int pkMsgid;

        @Expose
        @SerializedName("MessageBy")
        private String MessageBy;

        public String getMessageBy() {
            return MessageBy;
        }

        public void setMessageBy(String messageBy) {
            MessageBy = messageBy;
        }

        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
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
        @SerializedName("intDocumentTypeId")
        private int intdocumenttypeid;
        @Expose
        @SerializedName("SubjectId")
        private int subjectid;
        @Expose
        @SerializedName("Subject")
        private String subject;
        @Expose
        @SerializedName("Pk_TicketNo")
        private String pkTicketno;

        @Expose
        @SerializedName("UpdatedDate")
        private String UpdatedDate;

        @Expose
        @SerializedName("DeletedDate")
        private String DeletedDate;

        @Expose
        @SerializedName("ResolvedDate")
        private String ResolvedDate;

        @Expose
        @SerializedName("SupportMsgModels")
        private String SupportMsgModels;

        @Expose
        @SerializedName("strAttachment")
        private String strAttachment;

        @Expose
        @SerializedName("strTransactionNo")
        private String strTransactionNo;

        @Expose
        @SerializedName("ImageFile")
        private String ImageFile;

        public boolean isIsdeleted() {
            return isdeleted;
        }

        public boolean isIsactive() {
            return isactive;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            UpdatedDate = updatedDate;
        }

        public String getDeletedDate() {
            return DeletedDate;
        }

        public void setDeletedDate(String deletedDate) {
            DeletedDate = deletedDate;
        }

        public String getResolvedDate() {
            return ResolvedDate;
        }

        public void setResolvedDate(String resolvedDate) {
            ResolvedDate = resolvedDate;
        }

        public String getSupportMsgModels() {
            return SupportMsgModels;
        }

        public void setSupportMsgModels(String supportMsgModels) {
            SupportMsgModels = supportMsgModels;
        }

        public String getStrAttachment() {
            return strAttachment;
        }

        public void setStrAttachment(String strAttachment) {
            this.strAttachment = strAttachment;
        }

        public String getStrTransactionNo() {
            return strTransactionNo;
        }

        public void setStrTransactionNo(String strTransactionNo) {
            this.strTransactionNo = strTransactionNo;
        }

        public String getImageFile() {
            return ImageFile;
        }

        public void setImageFile(String imageFile) {
            ImageFile = imageFile;
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

        public String getPkTicketno() {
            return pkTicketno;
        }

        public void setPkTicketno(String pkTicketno) {
            this.pkTicketno = pkTicketno;
        }
    }
}
