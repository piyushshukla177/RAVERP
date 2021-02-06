package com.rav.raverp.data.model.api;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rav.raverp.R;

import java.io.Serializable;

public class ApiUploadImageResponse implements Serializable {

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Image")
    @Expose
    private String image;
    private final static long serialVersionUID = -1564320608974479857L;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String toString() {
        return image;
    }
    @BindingAdapter({"imageUrls"})
    public  static void loadImage(ImageView imageView, String imageUrl) {

        String img=imageUrl;
        Glide.with(imageView.getContext()).load("https://ravgroup.org" + imageUrl)
                .placeholder(R.drawable.account)
                .into(imageView);



    }
}