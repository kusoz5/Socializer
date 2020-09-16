package com.example.android.socializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import static android.content.Context.MODE_PRIVATE;

public class HttpRequest {

    Bitmap bitmap;

    HttpURLConnection httpURLConnection;

    saveCookie saveCookie;

    Context context;

    public HttpRequest(Context context) {
        this.saveCookie = new saveCookie(context);

        this.context = context;
    }

    public String makeHttpRequest(String link, String page) {

        saveCookie = new saveCookie(context);

        InputStream inputStream;

        String response = null;


        try {

            URL url = createURL(link);

            httpURLConnection = (HttpURLConnection) url.openConnection();


            setConstantHeaders(httpURLConnection, page);


            httpURLConnection.connect();



            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                response = toString(inputStream);
                processResponse(response, page, httpURLConnection);

            } else {
                inputStream = httpURLConnection.getErrorStream();
                response = toString(inputStream);
                if (response.contains("checkpoint_required")) {
                    SecurityCheck.check = true;
                }

                handleSecuritycheck(response);

            }


        } catch (Exception e) {

            Log.e("Exception While ", "makeHttpRequest: " + e.toString() + "   " + page + " " + link);
            boolean network = isNetworkAvailable();
            if (!network) {
             //   Toast.makeText(context, "Check Your Internet Connectivity", Toast.LENGTH_LONG).show();
            }
        }
        return response;

    }

    private void handleSecuritycheck(String response) throws Exception {

        if (response.contains("checkpoint_required")) {

            JSONObject root = new JSONObject(response);
            String message = root.getString("message");
            String url = root.getString("checkpoint_url");
            Boolean lock = root.getBoolean("lock");
            String status = root.getString("status");
            url = "https://www.instagram.com" + url;
            SecurityCheck.checkUrl = url;
            verifysecuritycheck(url);


        }

    }

    private void verifysecuritycheck(String link) {
        URL url = createURL(link);

        String csrf = saveCookie.getcookie("CSRF", null);

        String mid = saveCookie.getcookie("MID", null);

        String rur = saveCookie.getcookie("RUR", null);


        HttpURLConnection httpURLConnection = null;
        InputStream inputStream;
        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Host", "www.instagram.com");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Referer", "https://www.instagram.com/accounts/login/?source=auth_switcher");
            httpURLConnection.setRequestProperty("TE", "Trailers");
            httpURLConnection.setRequestProperty("Cookie", "csrftoken=" + csrf + "; rur=" + rur + "; mid=" + mid);
            httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            String response = toString(inputStream);
            processSecurityCheck(response);

        } catch (Exception e) {
            Log.e("Verify security check", "verifysecuritycheck: " + e.toString());
            if (!isNetworkAvailable()) {
               // Toast.makeText(context, "Check Your Internet Connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void processSecurityCheck(String response) {


        int inital = response.indexOf("window._sharedData =");
        int finalinde = response.indexOf("};</script>");
        String json = response.substring(inital + 21, finalinde) + "}";
        try {
            JSONObject root = new JSONObject(json);
            JSONObject entry_data = root.getJSONObject("entry_data");
            JSONArray Challenge = entry_data.getJSONArray("Challenge");

            JSONObject challengeroot = Challenge.getJSONObject(0);
            JSONObject feilds = challengeroot.getJSONObject("fields");

            if (feilds.has("email")) {
                SecurityCheck.email = feilds.getString("email");
            } else {
                SecurityCheck.email = "null";

            }
            if (feilds.has("phone_number")) {
                SecurityCheck.phone = feilds.getString("phone_number");

            } else {
                SecurityCheck.phone = "null";
            }
               SecurityCheck.rollout_hast = root.getString("rollout_hash");

        } catch (JSONException e) {
            Log.e("JSON", "processSecurityCheck: " + e);
        }


    }

    private URL createURL(String link) {

        URL url = null;

        try {

            url = new URL(link);

        } catch (Exception e) {

            Log.e("Excepiton while ", "createURL: " + e.toString());
        }

        return url;


    }

    private void setConstantHeaders(HttpURLConnection urlConnection, String page) throws Exception {

        urlConnection.setRequestProperty("Host", "www.instagram.com");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
        urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        GlobalAccessibleClass.UpdateValue = "Prog";
        switch (page) {


            case "landingPage":
                try {
                     GlobalAccessibleClass.httpRequestAsynce.ProgressPublish(0);
                    urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                    urlConnection.setRequestProperty("Connection", "keep-alive");

                    urlConnection.setRequestMethod("GET");

                } catch (Exception e) {
                    Log.e("url", "setConstantHeaders: " + e.toString());
                }
                break;
            case "loginAuth":
                try {
                    GlobalAccessibleClass.httpRequestAsynce.ProgressPublish(1);
                    String csrf = HTTP.CSRF;
                    String mid = HTTP.MID;
                    String rur = HTTP.rur;
                    String ajax = HTTP.Ajax;
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", " */*");//Especial for this
                    urlConnection.setRequestProperty("Connection", "keep-alive");
                    urlConnection.setRequestProperty("Referer", "https://www.instagram.com/accounts/login/");//Especial for this
                    urlConnection.setRequestProperty("X-CSRFToken", csrf);//Especial for this
                    urlConnection.setRequestProperty("X-Instagram-AJAX", ajax);//Especial for this
                    urlConnection.setRequestProperty("X-IG-App-ID", "936619743392459");//Especial for this
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//Especial for this
                    urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");//Especial for this
                    urlConnection.setRequestProperty("Cookie", "csrftoken=" + csrf + "; rur=" + rur + "; mid=" + mid);
                    urlConnection.setDoOutput(true);//Especial for this
                    String urlParameters = "username=" + HTTP.username + "&password=" + HTTP.password + "&queryParams={}&optIntoOneTap=true";
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(urlParameters);
                    writer.flush();


                } catch (Exception e) {
                    Log.e("Shared Prefrence ", "setConstantHeaders: " + e.toString());
                }


                break;

            case "reelids":

                String csrf = saveCookie.getcookie("CSRF", null);

                String mid = saveCookie.getcookie("MID", null);

                String rur = saveCookie.getcookie("RUR", null);

                String ds_userid = saveCookie.getcookie("DS_USERID", null);

                String urlgent = saveCookie.getcookie("URLGEN", null);

                String session = saveCookie.getcookie("Session_ID", null);

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Referer", "https://www.instagram.com/");
                urlConnection.setRequestProperty("Accept", " */*");//Especial for this
                urlConnection.setRequestProperty("Cookie", "csrftoken=" + csrf + "; mid=" + mid + "; ds_user_id=" + ds_userid + "; sessionid=" + session + "; rur=" + rur + "; urlgen=" + urlgent);

                break;

            case "stories":
                String csrf1 = saveCookie.getcookie("CSRF", null);

                String mid1 = saveCookie.getcookie("MID", null);

                String rur1 = saveCookie.getcookie("RUR", null);

                String ds_userid1 = saveCookie.getcookie("DS_USERID", null);

                String urlgent1 = saveCookie.getcookie("URLGEN", null);

                String session1 = saveCookie.getcookie("Session_ID", null);

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
                urlConnection.setRequestProperty("Cache-Control", "max-age=0");
                urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                urlConnection.setRequestProperty("Connection", "keep-alive");
                urlConnection.setRequestProperty("Cookie", "csrftoken=" + csrf1 + "; mid=" + mid1 + "; ds_user_id=" + ds_userid1 + "; sessionid=" + session1 + "; rur=" + rur1 + "; urlgen=" + urlgent1);


        }


        httpURLConnection = urlConnection;


    }


    private void processResponse(String response, String page, HttpURLConnection httpURLConnection) {


        switch (page) {

            case "landingPage":
                processLanding(response, page, httpURLConnection);
                break;

            case "loginAuth":
                processLoginAuth(response, page, httpURLConnection);
                break;

            case "reelids":
                processReelIds(response, page, httpURLConnection);
                break;

            case "stories":
                HTTP.storyResponse = response;

                break;
        }

    }

    private void processReelIds(String response, String page, HttpURLConnection httpURLConnection) {


        try {
            JSONObject root = new JSONObject(response);
            JSONObject data = root.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");
            JSONObject feed_reels_tray = user.getJSONObject("feed_reels_tray");
            JSONObject edge_reels_tray_to_reel = feed_reels_tray.getJSONObject("edge_reels_tray_to_reel");
            JSONArray edges = edge_reels_tray_to_reel.getJSONArray("edges");
            int number_of_story = edges.length();
            String reelid = "[";
            for (int i = 0; i < number_of_story; i++) {
                JSONObject each_Story = edges.getJSONObject(i);
                JSONObject node = each_Story.getJSONObject("node");
                JSONObject owener = node.getJSONObject("owner");
                String username = owener.getString("username");
                GettingBitmap gettingBitmap = new GettingBitmap();
                gettingBitmap.execute(owener.getString("profile_pic_url"), username);
                String reel_id = node.getString("id");
                if (i != number_of_story - 1) {
                    reelid = reelid + '"' + reel_id + '"' + ",";
                } else {
                    reelid = reelid + '"' + reel_id + '"' + "]";
                }

            }
            Log.e("Reel Id ", reelid);
            String URL = "https://www.instagram.com/graphql/query/?query_hash=cda12de4f7fd3719c0569ce03589f4c4&variables={\"reel_ids\":" + reelid + ",\"tag_names\":[],\"location_ids\":[],\"highlight_reel_ids\":[],\"precomposed_overlay\":false,\"show_story_viewer_list\":true,\"story_viewer_fetch_count\":50,\"story_viewer_cursor\":\"\",\"stories_video_dash_manifest\":false}";
            makeHttpRequest(URL, "stories");


        } catch (Exception e) {
            Log.e("Parse Json", e.toString());
            HTTP.storyResponse = "SessionExpired";
            saveCookie.savecookie("Authenticated", "false");
            return;
        }
    }

    private void processLoginAuth(String response, String page, HttpURLConnection httpURLConnection) {
        Log.e("Process Login", response);
        if (response.charAt(18) == 't') {
            try {
                JSONObject root = new JSONObject(response);
                String url = root.getString("profilePictureUrl");
                saveCookie.savecookie("ProfileInstaUser",url);

            }
            catch (Exception e){
                Log.e("Profile Pic",response);
            }

            saveCookie.savecookie("Authenticated", "true");

            Map<String, List<String>> header = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : header.entrySet()) {
                String key = entry.getKey();


                for (String string : entry.getValue()) {

                    if (string.contains("sessionid")) {
                        int inital = string.indexOf("=") + 1;
                        int finali = string.indexOf(";");
                        HTTP.Session_ID = string.substring(inital, finali);
                        saveCookie.savecookie("Session_ID", HTTP.Session_ID);


                    }
                    if (string.contains("urlgen")) {
                        int inital = string.indexOf("=") + 1;
                        int finali = string.indexOf(";");
                        HTTP.URLGEN = string.substring(inital, finali);

                        saveCookie.savecookie("URLGEN", HTTP.URLGEN);


                    }
                    if (string.contains("ds_user_id")) {
                        int inital = string.indexOf("=") + 1;
                        int finali = string.indexOf(";");
                        HTTP.DS_USERID = string.substring(inital, finali);

                        saveCookie.savecookie("DS_USERID", HTTP.DS_USERID);


                    }
                    if (string.contains("shbts")) {
                        int inital = string.indexOf("=") + 1;
                        int finali = string.indexOf(";");
                        HTTP.SHBTS = string.substring(inital, finali);
                        saveCookie.savecookie("SHBTS", HTTP.SHBTS);


                    }
                    if (string.contains("shbid")) {
                        int inital = string.indexOf("=") + 1;
                        int finali = string.indexOf(";");
                        HTTP.SHBID = string.substring(inital, finali);
                        saveCookie.savecookie("SHBID", HTTP.SHBID);


                    }

                }


            }


        } else {
            saveCookie.savecookie("Authenticated", "false");
        }

    }

    private void processLanding(String response, String page, HttpURLConnection httpURLConnection) {


        int index = response.indexOf("\"rollout_hash\"");
        int findexl = response.indexOf("\"bundle_variant\"");

        HTTP.Ajax = response.substring(index + 16, findexl - 2);

        saveCookie.savecookie("AJAX", HTTP.Ajax);


        Map<String, List<String>> header = httpURLConnection.getHeaderFields();


        for (Map.Entry<String, List<String>> entry : header.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (String string : value) {

                if (string.contains("mid")) {
                    int findex = string.indexOf(";");
                    int iindex = string.indexOf("=");


                    HTTP.MID = string.substring(iindex + 1, findex);
                    saveCookie.savecookie("MID", HTTP.MID);


                }
                if (string.contains("rur")) {
                    int findex = string.indexOf(";");
                    int iindex = string.indexOf("=");


                    HTTP.rur = string.substring(iindex + 1, findex);
                    saveCookie.savecookie("RUR", HTTP.rur);


                }
                if (string.contains("csrftoken")) {
                    int findex = string.indexOf(";");
                    int iindex = string.indexOf("=");


                    HTTP.CSRF = string.substring(iindex + 1, findex);
                    saveCookie.savecookie("CSRF", HTTP.CSRF);

                }
            }


        }


        makeHttpRequest(HTTP.loginAuth, "loginAuth");
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

    public class GettingBitmap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String username) {
            HTTP.usernamewithdp.put(username, bitmap);
        }

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());

            } catch (Exception e) {
                Log.e("Bitmap", e.toString());
                boolean network = isNetworkAvailable();
                if (!network) {
                  //  Toast.makeText(context, "Check Your Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }
            return strings[1];


        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean network = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(!network) {
            GlobalAccessibleClass.isNetwrok = false;
        } return network;
    }

    public String selectSecuritCheckMethod(int selected) {
        saveCookie saveCookie = new saveCookie(context);
String response=null;
        String Xcsrf = saveCookie.getcookie("CSRF",null);
        String MID   =   saveCookie.getcookie("MID",null);
        String RUR = saveCookie.getcookie("RUR",null);
        String shbid = saveCookie.getcookie("SHBID",null);
        URL url = createURL(SecurityCheck.checkUrl);
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Host", "www.instagram.com");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpURLConnection.setRequestProperty("X-CSRFToken",Xcsrf);
            httpURLConnection.setRequestProperty("X-Instagram-AJAX", SecurityCheck.rollout_hast);
            httpURLConnection.setRequestProperty("X-IG-App-ID", "936619743392459");
            httpURLConnection.setRequestProperty("Referer", SecurityCheck.checkUrl);
            httpURLConnection.setRequestProperty("Cookie", "mid="+MID + "; shbid="+ shbid + "; csrftoken=" +Xcsrf +"; rur="+RUR);
            httpURLConnection.setDoOutput(true);
            Log.e("Choice & Sel",SecurityCheck.choice + ":" + SecurityCheck.selected);
            String postData = "choice="+ SecurityCheck.choice;
            OutputStreamWriter writer  = new OutputStreamWriter(httpURLConnection.getOutputStream());
            writer.write(postData);
            httpURLConnection.connect();
            writer.flush();
            inputStream = httpURLConnection.getInputStream();
             response = toString(inputStream);
            processSecurityCheck(response);
        } catch (Exception e) {
            Log.e("", "selectSecuritCheckMethod: " + e.toString());
            isNetworkAvailable();
        }

          return response ;

    }

    public String submitSecurityCode(String URL){
        String isSecurityDone  = "false";
        String Xcsrf = saveCookie.getcookie("CSRF",null);
        String MID   =   saveCookie.getcookie("MID",null);
        String RUR = saveCookie.getcookie("RUR",null);
        String Urlgen = saveCookie.getcookie("URLGEN",null);
        String shbid = saveCookie.getcookie("SHBID",null);
        URL submitSecurityCodeLink = createURL(URL);
        HttpURLConnection httpURLConnection ;

        try {
            httpURLConnection = (HttpURLConnection) submitSecurityCodeLink.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept","*/*");
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Cookie","mid="+ MID +"; shbid=" +shbid+"; rur="+ RUR+"; urlgen=" + Urlgen + "; csrftoken="+ Xcsrf );
            httpURLConnection.setRequestProperty("Host", "www.instagram.com");
            httpURLConnection.setRequestProperty("Referer",SecurityCheck.checkUrl);
            httpURLConnection.setRequestProperty("TE","Trailers");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpURLConnection.setRequestProperty("X-CSRFToken",Xcsrf);
            httpURLConnection.setRequestProperty("X-Instagram-AJAX", SecurityCheck.rollout_hast);
            httpURLConnection.setRequestProperty("X-IG-App-ID", "936619743392459");
            httpURLConnection.setDoOutput(true);

            String securityCode = "security_code=" + SecurityCheck.securityCode;
            OutputStreamWriter outputStream = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStream.write(securityCode);

            httpURLConnection.connect();
            outputStream.flush();

            if ( httpURLConnection.getResponseCode() == 400 ){
                 isSecurityDone ="False";
                saveCookie.savecookie("Authenticated", "false");

            }
            else if (httpURLConnection.getResponseCode() == 200){
                isSecurityDone  = "true" ;
                saveCookie.savecookie("Authenticated", "true");

                Map<String ,List<String>> header =  httpURLConnection.getHeaderFields();

                for(Map.Entry<String,List<String>> Entry  : header.entrySet()){
                    String key = Entry.getKey();


                    for (String string : Entry.getValue()) {

                        if (string.contains("sessionid")) {
                            int inital = string.indexOf("=") + 1;
                            int finali = string.indexOf(";");
                            HTTP.Session_ID = string.substring(inital, finali);
                            saveCookie.savecookie("Session_ID", HTTP.Session_ID);


                        }
                        if (string.contains("urlgen")) {
                            int inital = string.indexOf("=") + 1;
                            int finali = string.indexOf(";");
                            HTTP.URLGEN = string.substring(inital, finali);

                            saveCookie.savecookie("URLGEN", HTTP.URLGEN);


                        }
                        if (string.contains("ds_user_id")) {
                            int inital = string.indexOf("=") + 1;
                            int finali = string.indexOf(";");
                            HTTP.DS_USERID = string.substring(inital, finali);

                            saveCookie.savecookie("DS_USERID", HTTP.DS_USERID);


                        }
                        if (string.contains("shbts")) {
                            int inital = string.indexOf("=") + 1;
                            int finali = string.indexOf(";");
                            HTTP.SHBTS = string.substring(inital, finali);
                            saveCookie.savecookie("SHBTS", HTTP.SHBTS);


                        }
                        if (string.contains("shbid")) {
                            int inital = string.indexOf("=") + 1;
                            int finali = string.indexOf(";");
                            HTTP.SHBID = string.substring(inital, finali);
                            saveCookie.savecookie("SHBID", HTTP.SHBID);


                        }

                    }


                }

            }

        }

        catch (Exception e){
            Log.e("submitSecurity",e.toString());
            isNetworkAvailable();
        }
        return isSecurityDone;
    }
}





