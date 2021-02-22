package com.rav.raverp.data.model.api;

import android.net.Uri;



public class SendAttachmentModel {
    String name;
    Uri uri;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
