package com.example.android.socializer;

public class downloadLinkData {
   public static String thumbnail[];
    public static boolean isvideo[];
    public static String videoResources[];
     public static  String owner;
    public downloadLinkData(String thumbnail[], boolean[] isvideo, String[] download, String owner) {
        this.thumbnail = thumbnail;
        this.isvideo = isvideo;
        this.videoResources = download;
         this.owner = owner;
    }
    public downloadLinkData(){}

    public String[] getThumbnail() {
        return thumbnail;
    }

    public boolean[] isIsvideo() {
        return isvideo;
    }

    public String[] getVideoResources() {
        return videoResources;
    }
}
