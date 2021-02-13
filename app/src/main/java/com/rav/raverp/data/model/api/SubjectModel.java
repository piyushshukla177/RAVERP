package com.rav.raverp.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class SubjectModel implements Serializable {

    @Expose
    @SerializedName("isdeleted")
    private boolean isdeleted;
    @Expose
    @SerializedName("strSubjectName")
    private String strsubjectname;
    @Expose
    @SerializedName("IntSubjectId")
    private int intsubjectid;

    public boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getStrsubjectname() {
        return strsubjectname;
    }

    public void setStrsubjectname(String strsubjectname) {
        this.strsubjectname = strsubjectname;
    }

    public int getIntsubjectid() {
        return intsubjectid;
    }

    public void setIntsubjectid(int intsubjectid) {
        this.intsubjectid = intsubjectid;
    }

    @Override
    public String toString() {
        return strsubjectname;
    }

}
