package com.example.android.socializer;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.socializer.HTTP;

public class saveCookie {
    public final static String PREFS_NAME = "SHARED_PREFRENCE";

         Context context;

    public saveCookie(Context context) {
        this.context = context;
    }

    public void savecookie(String key, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }

    public String getcookie(String key,String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);





    }
}
