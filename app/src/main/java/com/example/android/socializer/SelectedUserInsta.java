package com.example.android.socializer;

import android.graphics.Bitmap;

public class SelectedUserInsta {
    public String VideoLink;
    public String ImageLink;
    public Boolean isVideo;
    public String  NmaeOfTheFile = "";
    public SelectedUserInsta(String videoLink,String imageLink, Boolean isVideo){
        this.ImageLink = imageLink;
        this.isVideo = isVideo;
                this.VideoLink  = videoLink;
    }
    public String getImageLink() {
        return ImageLink;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public String getVideoLink() {
        return VideoLink;
    }
}
