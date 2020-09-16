package com.example.android.socializer;

import android.graphics.Bitmap;

public class instaStoryListObject {


        public String profile_pic_url;
        public String instausername;
        public int items;
        public String[] linkarray ;
        public Bitmap Profile_Pic;
        public boolean[] isVideo ;
        public String VideoDisplay[];

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public String getInstausername() {
        return instausername;
    }

    public int getItems() {
        return items;
    }

    public String[] getVideoDisplay() {
        return VideoDisplay;
    }

    public String[] getLinkarray() {
        return linkarray;
    }

    public Bitmap getProfile_Pic() {
        return Profile_Pic;
    }

    public boolean[] getIsVideo() {
        return isVideo;
    }
}
