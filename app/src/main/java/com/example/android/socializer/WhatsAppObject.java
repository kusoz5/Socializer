package com.example.android.socializer;

import android.graphics.Bitmap;

public class WhatsAppObject {
    private Bitmap bitmap;
    private String VideoUri;
    private String WHATSAPP_IMAGE_PATH;

    public WhatsAppObject(Bitmap bitmap, String VideoUri) {
        this.bitmap = bitmap;
        this.VideoUri = VideoUri;
    }
    public WhatsAppObject(Bitmap bitmap,String WHATSAPP_IMAGE_PATH, String VideoUri) {
        this.bitmap = bitmap;
        this.VideoUri = VideoUri;
        this.WHATSAPP_IMAGE_PATH =WHATSAPP_IMAGE_PATH;
    }

    public String getWHATSAPP_IMAGE_PATH() {
        return WHATSAPP_IMAGE_PATH;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getVideoUri() {
        return VideoUri;
    }
}
