package com.example.android.socializer;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.arch.lifecycle.Lifecycle;
import android.content.ClipboardManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.cnrylmz.zionfiledownloader.ZionDownloadFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstagramPost extends Fragment implements  View.OnClickListener  {

    String copied;
    Switch downloadservice;
    String cfduid;
    List<String> CopiedLink= new ArrayList<>();
    String RequestVerificationToken;
     String uniquePath;
     boolean download;
    Boolean ManagerCreatedOrNot = false;
     Context context;
    Queue<downloadLinkData> list = new LinkedList<>();
    ClipboardManager manager;
    Button button;
    View view;
    public InstagramPost() {
        // Required empty public constructor
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(" onResume", "onResume: " );
        HomeActivity homeActivity = new HomeActivity();
        homeActivity.setBottom(2);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

                 manager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);




        downloadservice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    GlobalAccessibleClass.downloadServiceSwithced = true;
                    manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                        @Override
                        public void onPrimaryClipChanged() {

                                copied = manager.getText().toString();

                                if (copied.contains("https://www.instagram.com")) {

                                       downloadPostlinks downloadPostlinks =  new downloadPostlinks();
                                       String ModifiedLink  = copied.substring(0,copied.lastIndexOf("?")) + "?__a=1";

                                       downloadPostlinks.execute(ModifiedLink);




                                }



                        }
                    });
                    Toast.makeText(context, "Downlaod Post Service Has Been Started ", Toast.LENGTH_LONG).show();


                } else {
                    GlobalAccessibleClass.downloadServiceSwithced = false;

                    Toast.makeText(context, "Downlaod Post Service Has Been Stopped ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_instagram_post, container, false);
        downloadservice =  (Switch) view.findViewById(R.id.downloadswitch);

        downloadservice.setChecked(GlobalAccessibleClass.downloadServiceSwithced);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(button.getText().toString().equals("DownloadServiceFalse")) {
            button.setText("DownloadServiceTrue");
            GlobalAccessibleClass.downloadServiceButton="DownloadServiceTrue";
            manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    copied = manager.getText().toString();

                    if (copied.contains("https://www.instagram.com")) {
                        downloadPostlinks downloadPostlinks =  new downloadPostlinks();
                        String ModifiedLink  = copied.substring(0,copied.lastIndexOf("?")) + "?__a=1";
                        if(!CopiedLink.contains(ModifiedLink)) {
                            Toast.makeText(context,"Downaload Service stareted",Toast.LENGTH_SHORT);
                            downloadPostlinks.execute(ModifiedLink);
                        }
                        else
                        {
                            Toast.makeText(context,"Same Link Coppied More Than Once ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            button.setText("DownloadServiceFalse");
            GlobalAccessibleClass.downloadServiceButton="DownloadServiceFalse";

        }
    }


    public class downloadPostlinks extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {


                ParseJsonForPost(s);




    }

        @Override
        protected String doInBackground(String... strings) {
            return downloadpost(strings[0]);
        }
    }

    private void DownloadPost(downloadLinkData poll) {

        downloadLinkData downloadLinkData = poll;
try {

    Log.e("Downloading", "DownloadPost: "+poll.owner );

    if (downloadLinkData.thumbnail.length == 1) {

        if (!downloadLinkData.isvideo[0]) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLinkData.thumbnail[0]));
            request.setTitle("Socializer:" + downloadLinkData.owner);
            request.setDescription("Downloading Post of" + " " + downloadLinkData.owner);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir("/Socializer/Instagram/Post/Images/", downloadLinkData.owner + ".jpeg");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Toast.makeText(context,"Download Started",Toast.LENGTH_SHORT);
        } else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLinkData.videoResources[0]));
            request.setTitle("Sociallizer Downloading Post" + " :" + downloadLinkData.owner);
            request.setDescription("Downloading  Post of" + " " + downloadLinkData.owner);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationInExternalPublicDir("/Socializer/Instagram/Post/Videos/", downloadLinkData.owner + ".mp4");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            manager.enqueue(request);
            Toast.makeText(context,"Download Started",Toast.LENGTH_SHORT);


        }
    } else {

        for (int l = 0; l < downloadLinkData.thumbnail.length; l++) {
            if (!downloadLinkData.isvideo[l]) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLinkData.thumbnail[l]));
                request.setTitle("Socializer:" + downloadLinkData.owner);
                request.setDescription("Downloading Post of" + " " + downloadLinkData.owner);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir("/Socializer/Instagram/Post/Images/", downloadLinkData.owner + ".jpeg");
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(context,"Download Started",Toast.LENGTH_SHORT);


            } else {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLinkData.videoResources[l]));
                request.setTitle("Sociallizer Downloading Post" + " :" + downloadLinkData.owner);
                request.setDescription("Downloading  Post of" + " " + downloadLinkData.owner);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setDestinationInExternalPublicDir("/Socializer/Instagram/Post/Videos/", downloadLinkData.owner + ".mp4");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                manager.enqueue(request);
                Toast.makeText(context,"Download Started",Toast.LENGTH_SHORT);

            }


        }
    }
}
        catch (Exception e){
          Log.e("DownloadMnagar",e.toString());
            boolean network = isNetworkAvailable();
            if(!network) {
                Toast.makeText(context,"Check Your Internet Connectivity",Toast.LENGTH_LONG).show();
            }

        }
    }
    private String downloadpost(String string) {
        String response = null;
        HttpURLConnection urlConnection;
        InputStream inputStream;

        saveCookie saveCookie = new saveCookie(context);
        String csrf = saveCookie.getcookie("CSRF", null);

        String mid = saveCookie.getcookie("MID", null);

        String rur = saveCookie.getcookie("RUR", null);

        String ds_userid = saveCookie.getcookie("DS_USERID", null);

        String urlgent = saveCookie.getcookie("URLGEN", null);

        String session = saveCookie.getcookie("Session_ID", null);

        try {

            URL url = new URL(string);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Host", "www.instagram.com");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
            urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            urlConnection.setRequestProperty("Cache-Control", "max-age=0");
            urlConnection.setRequestProperty("TE", "Trailers");
            String cookie = "Cookie: csrftoken=" + csrf + "; rur=" + rur + "; mid=" + mid + "; ds_user_id=" + ds_userid + "; urlgen=" + urlgent + "; sessionid=" + session;
            urlConnection.setRequestProperty("Cookie", cookie);
            urlConnection.connect();
            response = toString(urlConnection.getInputStream());
        } catch (Exception e) {

            Log.e("Exception while ", "downloadpost: " + e.toString());


            boolean network = isNetworkAvailable();
            if(!network) {
                Toast.makeText(context,"Check Your Internet Connectivity",Toast.LENGTH_LONG).show();
            }

        }
        return response;

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

    private void ParseJsonForPost(String s) {
        String Thumbnail[];
        boolean videocheck[] ;
        String videolink[];
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject graphql = jsonObject.getJSONObject("graphql");
            JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");
            String owner = shortcode_media.getJSONObject("owner").getString("username");
            if( shortcode_media.has("edge_sidecar_to_children") ){
                JSONObject edge_sidecar_to_children = shortcode_media.getJSONObject("edge_sidecar_to_children");
                JSONArray edges = edge_sidecar_to_children.getJSONArray("edges");

                int length = edges.length();
                Log.e("Numbner ",String.valueOf(length));
                Thumbnail=  new String[length];
                videocheck = new boolean[length];
                 videolink= new String[length];

                for (int l = 0 ; l< length ; l++){

                    JSONObject eachPost = edges.getJSONObject(l);
                    JSONObject node  = eachPost.getJSONObject("node");
                    Thumbnail[l]  = node.getString("display_url");
                    videocheck[l] = node.getBoolean("is_video");

                    if ( videocheck[l]  ){

                        videolink[l] = node.getString("video_url");
                    }
                    else {
                        videolink[l] = null ;
                    }

                    Log.e("Each Post " + l," Thumbnail Link " + Thumbnail[l]);
                    Log.e("Each Post " + l," isVide Link " + videocheck[l]);
                    Log.e("Each Post "+ l," video Link " + videolink[l]);


                    //list.add( new downloadLinkData(Thumbnail,videocheck,videolink,owner));
                    DownloadPost(new downloadLinkData(Thumbnail,videocheck,videolink,owner));

                }

            }

            else {
                Thumbnail = new String[1];
                videolink= new String[1];
                videocheck= new boolean[1];

                Thumbnail[0] = shortcode_media.getString("display_url");
                videocheck[0] =shortcode_media.getBoolean("is_video");

                if( videocheck[0] ){

                    videolink[0] = shortcode_media.getString("video_url");
                }
                else {
                    videolink[0] = null;
                }
                Log.e("Each Post " + 0," Thumbnail Link " + Thumbnail[0]);
                Log.e("Each Post " + 0," isVide Link " + videocheck[0]);
                Log.e("Each Post "+ 0," video Link " + videolink[0]);
              // list.add(new downloadLinkData(Thumbnail,videocheck,videolink,owner));
                DownloadPost(new downloadLinkData(Thumbnail,videocheck,videolink,owner));

            }
        } catch (Exception e) {
               Log.e("ParseJsonForPost",e.toString());

           Toast.makeText(context,"Socializer\n" +
                   "Download Post \n" +
                   "Looks like requested Post to download is  private please consider to login with same account ",Toast.LENGTH_LONG).show();

        }


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
