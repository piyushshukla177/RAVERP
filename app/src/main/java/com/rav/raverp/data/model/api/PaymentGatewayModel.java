package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentGatewayModel implements Serializable
    {

        @SerializedName("EmailId")
        @Expose
        private String emailId;
        @SerializedName("AssociateLoginId")
        @Expose
        private String associateLoginId;
        @SerializedName("CustomerId")
        @Expose
        private String customerId;
        @SerializedName("RoleId")
        @Expose
        private Integer roleId;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("Amount")
        @Expose
        private String amount;
        @SerializedName("Payment_method")
        @Expose
        private String paymentMethod;
        @SerializedName("IsSuccessful")
        @Expose
        private String isSuccessful;
        @SerializedName("HashKey")
        @Expose
        private String hashKey;
        @SerializedName("SaltKey")
        @Expose
        private String saltKey;
        @SerializedName("Merchante_key")
        @Expose
        private String merchanteKey;
        @SerializedName("Transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("service_provider")
        @Expose
        private String serviceProvider;
        @SerializedName("productinfo")
        @Expose
        private String productinfo;
        @SerializedName("TransactionDate")
        @Expose
        private String transactionDate;
        @SerializedName("Url")
        @Expose
        private String url;
        @SerializedName("surl")
        @Expose
        private String surl;
        @SerializedName("Furl")
        @Expose
        private String furl;

        @SerializedName("Firstname")
        @Expose
        private String Firstname;

        public String getFirstname() {
            return Firstname;
        }

        public void setFirstname(String firstname) {
            Firstname = firstname;
        }

        private final static long serialVersionUID = 2623794375436155316L;

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getAssociateLoginId() {
            return associateLoginId;
        }

        public void setAssociateLoginId(String associateLoginId) {
            this.associateLoginId = associateLoginId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getIsSuccessful() {
            return isSuccessful;
        }

        public void setIsSuccessful(String isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        public String getHashKey() {
            return hashKey;
        }

        public void setHashKey(String hashKey) {
            this.hashKey = hashKey;
        }

        public String getSaltKey() {
            return saltKey;
        }

        public void setSaltKey(String saltKey) {
            this.saltKey = saltKey;
        }

        public String getMerchanteKey() {
            return merchanteKey;
        }

        public void setMerchanteKey(String merchanteKey) {
            this.merchanteKey = merchanteKey;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getServiceProvider() {
            return serviceProvider;
        }

        public void setServiceProvider(String serviceProvider) {
            this.serviceProvider = serviceProvider;
        }

        public String getProductinfo() {
            return productinfo;
        }

        public void setProductinfo(String productinfo) {
            this.productinfo = productinfo;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSurl() {
            return surl;
        }

        public void setSurl(String surl) {
            this.surl = surl;
        }

        public String getFurl() {
            return furl;
        }

        public void setFurl(String furl) {
            this.furl = furl;
        }

    }


