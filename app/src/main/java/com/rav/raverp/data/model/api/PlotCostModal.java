package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class PlotCostModal implements Serializable {


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
        @SerializedName("BookingAmount")
        private String bookingamount;
        @Expose
        @SerializedName("numericPlotAmount")
        private String numericplotamount;

        public String getBookingamount() {
            return bookingamount;
        }

        public void setBookingamount(String bookingamount) {
            this.bookingamount = bookingamount;
        }

        public String getNumericplotamount() {
            return numericplotamount;
        }

        public void setNumericplotamount(String numericplotamount) {
            this.numericplotamount = numericplotamount;
        }
    }
}
