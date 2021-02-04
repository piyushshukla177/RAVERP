package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public  class AddAssociateModal implements Serializable {
    @Expose
    @SerializedName("body")
    public List<BodyEntity> body;
    @Expose
    @SerializedName("Response")
    public String Response;
    @Expose
    @SerializedName("Message")
    public String Message;

    public List<BodyEntity> getBody() {
        return body;
    }

    public void setBody(List<BodyEntity> body) {
        this.body = body;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public static class BodyEntity {
        @Expose
        @SerializedName("Sponsorid")
        public int Sponsorid;
        @Expose
        @SerializedName("SimplePassword")
        public String SimplePassword;
        @Expose
        @SerializedName("Password")
        public String Password;
        @Expose
        @SerializedName("Mobile1")
        public String Mobile1;
        @Expose
        @SerializedName("DisplayName")
        public String DisplayName;
        @Expose
        @SerializedName("FirstName")
        public String FirstName;
        @Expose
        @SerializedName("Email")
        public String Email;
        @Expose
        @SerializedName("TransPassword")
        public String TransPassword;
        @Expose
        @SerializedName("LoginId")
        public String LoginId;
        @Expose
        @SerializedName("Leg")
        public String Leg;
        @Expose
        @SerializedName("bigintFK_ParentId")
        public int bigintFK_ParentId;
        @Expose
        @SerializedName("intFK_MemId")
        public int intFK_MemId;
        @Expose
        @SerializedName("Remark")
        public String Remark;
        @Expose
        @SerializedName("MSG")
        public int MSG;

        public int getSponsorid() {
            return Sponsorid;
        }

        public void setSponsorid(int sponsorid) {
            Sponsorid = sponsorid;
        }

        public String getSimplePassword() {
            return SimplePassword;
        }

        public void setSimplePassword(String simplePassword) {
            SimplePassword = simplePassword;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getMobile1() {
            return Mobile1;
        }

        public void setMobile1(String mobile1) {
            Mobile1 = mobile1;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String displayName) {
            DisplayName = displayName;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getTransPassword() {
            return TransPassword;
        }

        public void setTransPassword(String transPassword) {
            TransPassword = transPassword;
        }

        public String getLoginId() {
            return LoginId;
        }

        public void setLoginId(String loginId) {
            LoginId = loginId;
        }

        public String getLeg() {
            return Leg;
        }

        public void setLeg(String leg) {
            Leg = leg;
        }

        public int getBigintFK_ParentId() {
            return bigintFK_ParentId;
        }

        public void setBigintFK_ParentId(int bigintFK_ParentId) {
            this.bigintFK_ParentId = bigintFK_ParentId;
        }

        public int getIntFK_MemId() {
            return intFK_MemId;
        }

        public void setIntFK_MemId(int intFK_MemId) {
            this.intFK_MemId = intFK_MemId;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public int getMSG() {
            return MSG;
        }

        public void setMSG(int MSG) {
            this.MSG = MSG;
        }
    }


  /*  @SerializedName("MSG")
    @Expose
    private String MSG;
    @SerializedName("Remark")
    @Expose
    private String Remark;
    @SerializedName("intFK_MemId")
    @Expose
    private String intFK_MemId;
    @SerializedName("bigintFK_ParentId")
    @Expose
    private Integer bigintFK_ParentId;
    @SerializedName("Leg")
    @Expose
    private String Leg;
    @SerializedName("LoginId")
    @Expose
    private String LoginId;
    @SerializedName("TransPassword")
    @Expose
    private String TransPassword;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("FirstName")
    @Expose
    private String FirstName;
    @SerializedName("DisplayName")
    @Expose
    private String DisplayName;
    @SerializedName("Mobile1")
    @Expose
    private String Mobile1;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("SimplePassword")
    @Expose
    private String SimplePassword;
    @SerializedName("Sponsorid")
    @Expose
    private String Sponsorid;

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getIntFK_MemId() {
        return intFK_MemId;
    }

    public void setIntFK_MemId(String intFK_MemId) {
        this.intFK_MemId = intFK_MemId;
    }

    public Integer getBigintFK_ParentId() {
        return bigintFK_ParentId;
    }

    public void setBigintFK_ParentId(Integer bigintFK_ParentId) {
        this.bigintFK_ParentId = bigintFK_ParentId;
    }

    public String getLeg() {
        return Leg;
    }

    public void setLeg(String leg) {
        Leg = leg;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getTransPassword() {
        return TransPassword;
    }

    public void setTransPassword(String transPassword) {
        TransPassword = transPassword;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String mobile1) {
        Mobile1 = mobile1;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSimplePassword() {
        return SimplePassword;
    }

    public void setSimplePassword(String simplePassword) {
        SimplePassword = simplePassword;
    }

    public String getSponsorid() {
        return Sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        Sponsorid = sponsorid;
    }*/



     /* public String Response = "";
      public String Message = "";
      ArrayList<body> body;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<body> getBody() {
        return body;
    }

    public void setBody(ArrayList<body> body) {
        this.body = body;
    }

    public class body{
        public String MSG = "";
        public String Remark = "";
        public String intFK_MemId = "";
        public String bigintFK_ParentId = "";
        public String Leg = "";
        public String LoginId = "";
        public String TransPassword = "";
        public String Email = "";
        public String FirstName = "";
        public String DisplayName = "";
        public String Mobile1 = "";
        public String Password = "";
        public String SimplePassword = "";
        public String Sponsorid = "";


        public String getMSG() {
            return MSG;
        }

        public void setMSG(String MSG) {
            this.MSG = MSG;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getIntFK_MemId() {
            return intFK_MemId;
        }

        public void setIntFK_MemId(String intFK_MemId) {
            this.intFK_MemId = intFK_MemId;
        }

        public String getBigintFK_ParentId() {
            return bigintFK_ParentId;
        }

        public void setBigintFK_ParentId(String bigintFK_ParentId) {
            this.bigintFK_ParentId = bigintFK_ParentId;
        }

        public String getLeg() {
            return Leg;
        }

        public void setLeg(String leg) {
            Leg = leg;
        }

        public String getLoginId() {
            return LoginId;
        }

        public void setLoginId(String loginId) {
            LoginId = loginId;
        }

        public String getTransPassword() {
            return TransPassword;
        }

        public void setTransPassword(String transPassword) {
            TransPassword = transPassword;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String displayName) {
            DisplayName = displayName;
        }

        public String getMobile1() {
            return Mobile1;
        }

        public void setMobile1(String mobile1) {
            Mobile1 = mobile1;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getSimplePassword() {
            return SimplePassword;
        }

        public void setSimplePassword(String simplePassword) {
            SimplePassword = simplePassword;
        }

        public String getSponsorid() {
            return Sponsorid;
        }

        public void setSponsorid(String sponsorid) {
            Sponsorid = sponsorid;
        }
    }*/


}
