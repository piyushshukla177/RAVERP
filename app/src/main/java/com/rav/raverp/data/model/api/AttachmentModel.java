package com.rav.raverp.data.model.api;

import android.net.Uri;

import com.jaiselrahman.filepicker.model.MediaFile;

public class AttachmentModel {
    String name;
    MediaFile file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MediaFile getFile() {
        return file;
    }

    public void setFile(MediaFile file) {
        this.file = file;
    }
}
