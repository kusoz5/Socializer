package com.example.android.socializer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout mframeLayout;
    WhatsApp whatsApp = new WhatsApp();
    InstagramPost instagramPost = new InstagramPost();
    InstagramStory instagramStory = new InstagramStory();
    Downloads downloads =  new Downloads();
    boolean isloggin;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("", "onPostResume: " );
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     final    File file = new File("/data/data/"+getPackageName()+"/shared_prefs/"+HTTP.SHARED_PREFRENCE);

        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNav);
        GlobalAccessibleClass.bottomNavigationView = bottomNavigationView;
        mframeLayout = findViewById(R.id.mFrame);

        setFragment(whatsApp);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuWhatsapp:
                        setFragment(whatsApp);
                        return true;

                    case  R.id.menuInstaPost :
                        saveCookie saveCookie = new saveCookie(HomeActivity.this);
                        try {
                            String cookie = saveCookie.getcookie("Authenticated","false");
                            if(cookie.equals("true")) {
                               setFragment(instagramPost);
                            } else {
                                Intent intent =  new Intent(HomeActivity.this,loginInstagram.class);
                                startActivity(intent);
                            }
                        }catch (Exception e) {
                            Intent intent =  new Intent(HomeActivity.this,loginInstagram.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.menuInstaStory :

                            saveCookie saveCookie1 = new saveCookie(HomeActivity.this);
                            try {
                                String cookie = saveCookie1.getcookie("Authenticated","false");
                                if(cookie.equals("true")) {
                                    setFragment(instagramStory);
                                } else {
                                    Intent intent =  new Intent(HomeActivity.this,loginInstagram.class);
                                    startActivity(intent);
                                }
                            }catch (Exception e) {
                                Intent intent =  new Intent(HomeActivity.this,loginInstagram.class);
                                startActivity(intent);
                            }



                        return true;
                    case  R.id.menuDownloads:
                        setFragment(downloads);
                        return  true;
                }
                return true;
            }




        });

    }

    public void setFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mFrame,fragment);
       // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void setBottom(int id ) {


        switch (id) {
            case 0:
               GlobalAccessibleClass.bottomNavigationView.setSelectedItemId(R.id.menuWhatsapp);
                break;
            case 1:
                GlobalAccessibleClass.bottomNavigationView.setSelectedItemId(R.id.menuInstaStory);
                break;
            case 2:
                GlobalAccessibleClass.bottomNavigationView.setSelectedItemId(R.id.menuInstaPost);
                break;
            case 3:
                GlobalAccessibleClass.bottomNavigationView.setSelectedItemId(R.id.menuDownloads);
                break;
        }


    }

    private String islogged() {

        saveCookie saveCookie = new saveCookie(this);

        String csrf1 = saveCookie.getcookie("CSRF",null);

        String mid1 = saveCookie.getcookie("MID",null);

        String rur1 = saveCookie.getcookie("RUR",null);

        String ds_userid1 = saveCookie.getcookie("DS_USERID",null);

        String urlgent1 = saveCookie.getcookie("URLGEN",null);

        String session1 = saveCookie.getcookie("Session_ID",null);
        URL url = createURL("https://www.instagram.com/graphql/query/?query_hash=88d5c611344354c52c1fab7762c06f0b&variables={%22only_stories%22:true,%22stories_prefetch%22:true,%22stories_video_dash_manifest%22:false}");
        String Response  = "";
        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept","text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
            httpURLConnection.setRequestProperty("Accept-Language","en-US,en;q=0.5");
            httpURLConnection.setRequestProperty("Cache-Control","max-age=0");
            httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests","1");
            httpURLConnection.setRequestProperty("Connection","keep-alive");
            httpURLConnection.setRequestProperty("Cookie", "csrftoken="+csrf1+"; mid="+mid1+"; ds_user_id="+ds_userid1+"; sessionid="+session1+"; rur="+rur1+"; urlgen=" + urlgent1);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            Response = toString(inputStream);
        }
        catch (Exception e){
        }
        return Response;
    }
    private URL createURL(String link) {

        URL url = null;

        try {

            url = new URL(link);

        } catch (Exception e) {

            Log.e("Excepiton while ", "createURL: "+e.toString());
        }

        return url;


    }
    private String toString(InputStream inputStream) {

        StringBuilder stringBuilder = new StringBuilder();

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

        } catch (Exception e) {

            Log.e("StringBuilder", e.toString());
        }
        return stringBuilder.toString();

    }
    public class checkLogin extends AsyncTask<String,Void,String>{

        String FragmentCheck = "-1";
        @Override
        protected String doInBackground(String... strings) {
                FragmentCheck = strings[0];
            return islogged();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("{\"data\":{\"user\":null},\"status\":\"ok\"}")) {
                Intent intent =  new Intent(HomeActivity.this,loginInstagram.class);
                startActivity(intent);
            } else {
                isloggin = true;
                if ( FragmentCheck.equals("0") ) {
                    setFragment(instagramStory);
                }
                else {
                    setFragment(instagramPost);
                }

            }
        }
    }
    }




