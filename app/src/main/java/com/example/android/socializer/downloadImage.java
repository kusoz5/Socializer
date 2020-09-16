package com.example.android.socializer;

import android.graphics.Bitmap;

public class downloadImage {

    Bitmap bitmap;
String path;

    public String getPath() {
        return path;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public downloadImage(Bitmap bitmap,String path) {

        this.bitmap = bitmap;
        this.path = path;
    }
}
