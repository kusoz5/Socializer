package com.example.android.socializer;

import android.os.Environment;
import android.support.design.widget.BottomNavigationView;

import java.io.File;

public class GlobalAccessibleClass {
    public static WhatsAppObject videowhatsappobject ;
    public static WhatsAppObject imageWhatsAppObject;
    public static instaStoryListObject instaStoryListObject;
    public static boolean downloadServiceSwithced=false;
    public static SelectedUserInsta selectedUserInsta;
    static String  path = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String UpdateValue ;
   static BottomNavigationView bottomNavigationView;
   static loginInstagram.HttpRequestAsynce httpRequestAsynce ;
   static DownloadVideoDetails downloadVideoDetails = new DownloadVideoDetails();
   static boolean isNetwrok = true;

    static String downloadServiceButton ="DownlaodServiceFalse";

    static  File wifile = new File(path+"/Socializer/WhatsApp/Images/");

    static File wvfile = new File(path+"/Socializer/WhatsApp/Videos/");

    static File ipvfile = new File(path+"/Socializer/Instagram/Post/Videos/");

    static  File ipifile = new File(path+"/Socializer/Instagram/Post/Images/");

    static File isifile = new File(path+"/Socializer/Instagram/Story/Images/");

    static  File isvfile = new File(path+"/Socializer/Instagram/Story/Videos/");

}
