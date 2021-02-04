package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SendOtpForPlotBookingModel implements Serializable {

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
        @SerializedName("bigIntMobileOtpId")
        private int bigintmobileotpid;
        @Expose
        @SerializedName("dtGenerateOtp")
        private String dtgenerateotp;
        @Expose
        @SerializedName("OTP")
        private String otp;
        @Expose
        @SerializedName("Remark")
        private String remark;
        @Expose
        @SerializedName("Code")
        private int code;

        public int getBigintmobileotpid() {
            return bigintmobileotpid;
        }

        public void setBigintmobileotpid(int bigintmobileotpid) {
            this.bigintmobileotpid = bigintmobileotpid;
        }

        public String getDtgenerateotp() {
            return dtgenerateotp;
        }

        public void setDtgenerateotp(String dtgenerateotp) {
            this.dtgenerateotp = dtgenerateotp;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
