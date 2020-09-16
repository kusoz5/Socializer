package com.example.android.socializer;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.HashMap;

public class HTTP {


    final static String landingPage = "https://www.instagram.com/accounts/login/";

    SharedPreferences sharedPreferences ;

    final  static  String SHARED_PREFRENCE = "SHARED_PREFRENCE";

    SharedPreferences.Editor editor;

     static String username;

     static String reelids = "https://www.instagram.com/graphql/query/?query_hash=88d5c611344354c52c1fab7762c06f0b&variables={%22only_stories%22:true,%22stories_prefetch%22:true,%22stories_video_dash_manifest%22:false}";

     static String password;

    final static String loginAuth = "https://www.instagram.com/accounts/login/ajax/";

    static boolean authenticated = false;

    static  String Ajax;

    static String CSRF;

    static String MID;

    static String rur;

    static String SHBID;

    static String SHBTS;

    static String URLGEN;

    static String DS_USERID;

    static String Session_ID;

    static String storyResponse = null;

  static   HashMap<String, Bitmap> usernamewithdp = new HashMap<>();


}

