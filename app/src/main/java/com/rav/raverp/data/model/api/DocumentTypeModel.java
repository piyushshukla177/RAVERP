package com.rav.raverp.data.model.api;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DocumentTypeModel implements Serializable {

    @Expose
    @SerializedName("bitIsDelated")
    private boolean bitisdelated;
    @Expose
    @SerializedName("strDocument")
    private String strdocument;
    @Expose
    @SerializedName("intDocumentTypeId")
    private int intdocumenttypeid;

    public boolean getBitisdelated() {
        return bitisdelated;
    }

    public void setBitisdelated(boolean bitisdelated) {
        this.bitisdelated = bitisdelated;
    }

    public String getStrdocument() {
        return strdocument;
    }

    public void setStrdocument(String strdocument) {
        this.strdocument = strdocument;
    }

    public int getIntdocumenttypeid() {
        return intdocumenttypeid;
    }

    public void setIntdocumenttypeid(int intdocumenttypeid) {
        this.intdocumenttypeid = intdocumenttypeid;
    }

    public String toString() {
        return strdocument;
    }
}
