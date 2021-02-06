package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DashBoardModal implements Serializable {
    @Expose
    @SerializedName("body")
    private List<Body> body;
    @Expose
    @SerializedName("Response")
    private String response;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Body {
        @Expose
        @SerializedName("EliteClubCurrentYearTarget")
        private String eliteclubcurrentyeartarget;
        @Expose
        @SerializedName("EliteClubCurrentYearFP")
        private String eliteclubcurrentyearfp;
        @Expose
        @SerializedName("EliteClubCurrentYear")
        private String eliteclubcurrentyear;
        @Expose
        @SerializedName("EliteClubLastYearTarget")
        private String eliteclublastyeartarget;
        @Expose
        @SerializedName("EliteClubLastYearFP")
        private String eliteclublastyearfp;
        @Expose
        @SerializedName("EliteClubLastYear")
        private String eliteclublastyear;
        @Expose
        @SerializedName("FounderClubCurrentYearTarget")
        private String founderclubcurrentyeartarget;
        @Expose
        @SerializedName("FounderClubCurrentYearFP")
        private String founderclubcurrentyearfp;
        @Expose
        @SerializedName("FounderClubCurrentYear")
        private String founderclubcurrentyear;
        @Expose
        @SerializedName("FounderClubLastYearTarget")
        private String founderclublastyeartarget;
        @Expose
        @SerializedName("FounderClubLastYearFP")
        private String founderclublastyearfp;
        @Expose
        @SerializedName("FounderClubLastYear")
        private String founderclublastyear;
        @Expose
        @SerializedName("NetWorth")
        private String networth;
        @Expose
        @SerializedName("AchievedRewardsCount")
        private String achievedrewardscount;
        @Expose
        @SerializedName("Rank")
        private String rank;
        @Expose
        @SerializedName("TillDateIncome")
        private String tilldateincome;
        @Expose
        @SerializedName("LastFYIncome")
        private String lastfyincome;
        @Expose
        @SerializedName("QuarterlyIncome")
        private String quarterlyincome;
        @Expose
        @SerializedName("CurrentIncome")
        private String currentincome;
        @Expose
        @SerializedName("RightTeam")
        private String rightteam;
        @Expose
        @SerializedName("LeftTeam")
        private String leftteam;
        @Expose
        @SerializedName("InActiveMember")
        private String inactivemember;
        @Expose
        @SerializedName("ActiveMember")
        private String activemember;
        @Expose
        @SerializedName("hold")
        private String hold;
        @Expose
        @SerializedName("book")
        private String book;
        @Expose
        @SerializedName("TotalBV")
        private String totalbv;
        @Expose
        @SerializedName("RightBV")
        private String rightbv;
        @Expose
        @SerializedName("LeftBV")
        private String leftbv;
        @Expose
        @SerializedName("PersonalBV")
        private String personalbv;
        @Expose
        @SerializedName("RpWalletAmount")
        private String rpwalletamount;
        @Expose
        @SerializedName("MyWalletAmount")
        private String mywalletamount;

        public String getEliteclubcurrentyeartarget() {
            return eliteclubcurrentyeartarget;
        }

        public void setEliteclubcurrentyeartarget(String eliteclubcurrentyeartarget) {
            this.eliteclubcurrentyeartarget = eliteclubcurrentyeartarget;
        }

        public String getEliteclubcurrentyearfp() {
            return eliteclubcurrentyearfp;
        }

        public void setEliteclubcurrentyearfp(String eliteclubcurrentyearfp) {
            this.eliteclubcurrentyearfp = eliteclubcurrentyearfp;
        }

        public String getEliteclubcurrentyear() {
            return eliteclubcurrentyear;
        }

        public void setEliteclubcurrentyear(String eliteclubcurrentyear) {
            this.eliteclubcurrentyear = eliteclubcurrentyear;
        }

        public String getEliteclublastyeartarget() {
            return eliteclublastyeartarget;
        }

        public void setEliteclublastyeartarget(String eliteclublastyeartarget) {
            this.eliteclublastyeartarget = eliteclublastyeartarget;
        }

        public String getEliteclublastyearfp() {
            return eliteclublastyearfp;
        }

        public void setEliteclublastyearfp(String eliteclublastyearfp) {
            this.eliteclublastyearfp = eliteclublastyearfp;
        }

        public String getEliteclublastyear() {
            return eliteclublastyear;
        }

        public void setEliteclublastyear(String eliteclublastyear) {
            this.eliteclublastyear = eliteclublastyear;
        }

        public String getFounderclubcurrentyeartarget() {
            return founderclubcurrentyeartarget;
        }

        public void setFounderclubcurrentyeartarget(String founderclubcurrentyeartarget) {
            this.founderclubcurrentyeartarget = founderclubcurrentyeartarget;
        }

        public String getFounderclubcurrentyearfp() {
            return founderclubcurrentyearfp;
        }

        public void setFounderclubcurrentyearfp(String founderclubcurrentyearfp) {
            this.founderclubcurrentyearfp = founderclubcurrentyearfp;
        }

        public String getFounderclubcurrentyear() {
            return founderclubcurrentyear;
        }

        public void setFounderclubcurrentyear(String founderclubcurrentyear) {
            this.founderclubcurrentyear = founderclubcurrentyear;
        }

        public String getFounderclublastyeartarget() {
            return founderclublastyeartarget;
        }

        public void setFounderclublastyeartarget(String founderclublastyeartarget) {
            this.founderclublastyeartarget = founderclublastyeartarget;
        }

        public String getFounderclublastyearfp() {
            return founderclublastyearfp;
        }

        public void setFounderclublastyearfp(String founderclublastyearfp) {
            this.founderclublastyearfp = founderclublastyearfp;
        }

        public String getFounderclublastyear() {
            return founderclublastyear;
        }

        public void setFounderclublastyear(String founderclublastyear) {
            this.founderclublastyear = founderclublastyear;
        }

        public String getNetworth() {
            return networth;
        }

        public void setNetworth(String networth) {
            this.networth = networth;
        }

        public String getAchievedrewardscount() {
            return achievedrewardscount;
        }

        public void setAchievedrewardscount(String achievedrewardscount) {
            this.achievedrewardscount = achievedrewardscount;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getTilldateincome() {
            return tilldateincome;
        }

        public void setTilldateincome(String tilldateincome) {
            this.tilldateincome = tilldateincome;
        }

        public String getLastfyincome() {
            return lastfyincome;
        }

        public void setLastfyincome(String lastfyincome) {
            this.lastfyincome = lastfyincome;
        }

        public String getQuarterlyincome() {
            return quarterlyincome;
        }

        public void setQuarterlyincome(String quarterlyincome) {
            this.quarterlyincome = quarterlyincome;
        }

        public String getCurrentincome() {
            return currentincome;
        }

        public void setCurrentincome(String currentincome) {
            this.currentincome = currentincome;
        }

        public String getRightteam() {
            return rightteam;
        }

        public void setRightteam(String rightteam) {
            this.rightteam = rightteam;
        }

        public String getLeftteam() {
            return leftteam;
        }

        public void setLeftteam(String leftteam) {
            this.leftteam = leftteam;
        }

        public String getInactivemember() {
            return inactivemember;
        }

        public void setInactivemember(String inactivemember) {
            this.inactivemember = inactivemember;
        }

        public String getActivemember() {
            return activemember;
        }

        public void setActivemember(String activemember) {
            this.activemember = activemember;
        }

        public String getHold() {
            return hold;
        }

        public void setHold(String hold) {
            this.hold = hold;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getTotalbv() {
            return totalbv;
        }

        public void setTotalbv(String totalbv) {
            this.totalbv = totalbv;
        }

        public String getRightbv() {
            return rightbv;
        }

        public void setRightbv(String rightbv) {
            this.rightbv = rightbv;
        }

        public String getLeftbv() {
            return leftbv;
        }

        public void setLeftbv(String leftbv) {
            this.leftbv = leftbv;
        }

        public String getPersonalbv() {
            return personalbv;
        }

        public void setPersonalbv(String personalbv) {
            this.personalbv = personalbv;
        }

        public String getRpwalletamount() {
            return rpwalletamount;
        }

        public void setRpwalletamount(String rpwalletamount) {
            this.rpwalletamount = rpwalletamount;
        }

        public String getMywalletamount() {
            return mywalletamount;
        }

        public void setMywalletamount(String mywalletamount) {
            this.mywalletamount = mywalletamount;
        }
    }

}