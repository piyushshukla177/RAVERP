package com.rav.raverp.data.model.api;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class ForgotpasswordModel implements Serializable
    {

        @SerializedName("OTP")
        @Expose
        private String oTP;
        @SerializedName("OTPTime")
        @Expose
        private String oTPTime;
        @SerializedName("UserId")
        @Expose
        private String userId;
        private final static long serialVersionUID = 8719217762490908318L;

        public String getOTP() {
            return oTP;
        }

        public void setOTP(String oTP) {
            this.oTP = oTP;
        }

        public String getOTPTime() {
            return oTPTime;
        }

        public void setOTPTime(String oTPTime) {
            this.oTPTime = oTPTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }